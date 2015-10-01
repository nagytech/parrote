(($) ->

  me = ''

  ### Check if we are logged in as a user ###
  checkWhoami = (callback) ->
    $.ajax
      url: '/api/whoami'
    .done (username) ->
      callback username
      return
    .fail ->
      return
    return

  onLogoutSubmit = ->
    $('form#logout-form').submit()
    return

  ### Modify the UI to reflect that the user is logged in ###
  displayAsUser = (username) ->
    # Iterate each hidden element and remove the hidden attribute
    $('[data-state=logged-in][hidden]').each (i, e) =>
      $(e).removeAttr('hidden')
    return username

  ### Initialization ###
  init = ->
    # Check for logged in user and callback to change UI accordingly
    me = checkWhoami displayAsUser
    return

  $.fn.bonmotReact = (rjs) ->

    clearState = () ->
      rjs.setState
        items: []

    updateState = (data) ->
      items = rjs.state.items
      dataArray = if !$.isArray(data) then [ data ] else data

      for i in [dataArray.length - 1..0] by -1
        dataArrayItem = dataArray[i]
        items.unshift
          text: dataArrayItem.text,
          username: dataArrayItem.username,
          createdOn: new Date dataArrayItem.createdOn

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
        return

      # Close the websocket if a new search is submitted
      $('form#search button').one 'click', () ->
        clearState()
        ws.close()
        return

      return


    $('#new-message button').on 'click', onNewMessageSubmit
    $('#search button').on 'click', onSearchSubmit

    return this

  $('a#logout').on 'click', onLogoutSubmit

  ### Entry Point ###
  init()
  return

) jQuery