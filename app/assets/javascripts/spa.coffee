(($) ->

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
    return

  ### Initialization ###
  init = ->
    # Check for logged in user and callback to change UI accordingly
    checkWhoami displayAsUser
    return

  $.fn.bonmotReact = (rjs) ->

    ### Submit a new bonmot to the server and update the local UI ###
    submitBonMot = (text, callback) ->

      # TODO: If WS is connected, then disconnect the rjs updates (otherwise we get dupes)

      # Call to api to post message
      $.ajax
        url: '/api/postmessage'
        data: message: text
        type: 'POST'
      # On Success, update the UI
      .done (data) ->
        console.log "TODO: get/set state"
        items = rjs.state.items
        items.push
          text: data.text,
          username: data.username,
          createdOn: new Date data.createdOn
        rjs.setState
          items: items
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
      ws.onMessage = (e) ->
        console.log e
        return

      # Handle the ws open
      ws.onOpen = (e) ->
        return

      # Close the websocket if a new search is submitted
      $('form#search button').one 'click', () ->
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