(($) ->

  ### Check if we are logged in as a user ###
  checkWhoami = (callback) ->
    # Submit to the server a request for the current logged in username
    $.ajax
      url: '/api/whoami'
    .done (username) ->
      # Execute callback with result
      callback username
      return
    .fail ->
      return
    return

  ### Handle the on logout submit event ###
  onLogoutSubmit = ->
    # Log the current user out by submitting the logout form
    $('form#logout-form').submit()
    return

  ### Modify the UI to reflect that the user is logged in ###
  displayAsUser = (username) ->
    # Iterate each hidden element and remove the hidden attribute
    $('[data-state=logged-in][hidden]').each (i, e) =>
      $(e).removeAttr('hidden')
    return

  ### Initialization ###
  init = ->
    # Check for logged in user and callback to change UI accordingly
    checkWhoami displayAsUser
    return

  ### Create a jQuery plugin to consume the ReactJs object ###
  $.fn.bonmotReact = (rjs) ->

    # Clear items in the react object
    clearState = () ->
      rjs.setState
        items: []

    # Update the state using the given data object
    updateState = (data) ->

      # Force the data into an iterateable array
      dataArray = if !$.isArray(data) then [ data ] else data

      # Get the current state from the rjs object
      items = rjs.state.items

      # Iterate the provided data and prepend to the rjs state object
      # Iterate in reverse to handle old records first in order to
      # unshift new records into the top
      for i in [dataArray.length - 1..0] by -1

        dataArrayItem = dataArray[i]

        # Unshift here because we always want to insert new records onthe top
        items.unshift
          text: dataArrayItem.text,
          username: dataArrayItem.username,
          createdOn: new Date dataArrayItem.createdOn

      # Re-set the rjs state object with the newly unshifted entries
      rjs.setState
        items: items

      return

    ### Submit a new bonmot to the server ###
    submitBonMot = (text, callback) ->
      # Call to api to post message
      $.ajax
        url: '/api/postmessage'
        data: message: text
        type: 'POST'
      # On Success
      .done (data) ->
        # NOTE: No requirement to update the UI from here
        # Only update the UI if it matches the search results AND if a WS is open
        callback true
        return
      # On failure, popup a warning.
      .fail () ->
        callback
        return
      return

    onNewMessageSubmit = (e) ->
      # Prevent default behaviour
      e.preventDefault()

      # Scrape elements and properties
      textarea = $('#new-message textarea')
      button = $(this)
      text = textarea.val()

      # Update UI state to reflect that we're busy
      button.prop('disabled', true)
      textarea.prop('disabled', true)
      $("body").css("cursor", "progress")

      # Callback to restore UI state
      notBusy = (success) ->
        button.prop('disabled', false)
        textarea.prop('disabled', false)
        if (success) then textarea.val('')
        $('body').css('cursor', 'default')

      # Submit the message to the server
      submitBonMot(text, notBusy)

      return

    ### Handle the on search submit event ###
    onSearchSubmit = (e) ->

      e.preventDefault()

      # Init the ws based on search parameter
      ws = new WebSocket jsRoutes.controllers.LiveSearch.connect($('form#search input').val()).webSocketURL()

      # Handle error
      ws.onError = (e) ->
        return

      # Handle incoming message
      ws.onmessage = (e) ->
        updateState JSON.parse(e.data)
        return

      # Handle the ws open
      ws.onopen = (e) ->
        return

      ws.onclose = (e) ->
        # TODO: Could probably warn the user that we've been disconnected
        return

      # Close the websocket if a new search is submitted, also clear the current state
      $('form#search button').one 'click', () ->
        clearState()
        ws.close()
        return

      return


    ### Event hooks for driving functionality ###

    # Handle a new bonmot submission
    $('#new-message button').on 'click', onNewMessageSubmit

    # Handle a new search submission
    $('#search button').on 'click', onSearchSubmit

    return this

  # Handle the logout form
  $('a#logout').on 'click', onLogoutSubmit

  ### Entry Point ###
  init()
  return

) jQuery