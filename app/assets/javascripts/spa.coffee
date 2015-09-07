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

  ### Submit a new bonmot to the server and update the local UI ###
  submitBonMot = (text, callback) ->
    # Call to api to post message
    $.ajax
      url: '/api/postmessage'
      data: message: text
      type: 'POST'
    # On Success, update the UI
    .done (data) ->
      # TODO: Update UI with new bonmot
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

  ### Event Handlers ###
  $('#new-message button').on 'click', onNewMessageSubmit
  $('#search button').on 'click', onSearchSubmit
  $('a#logout').on 'click', onLogoutSubmit

  ### Entry Point ###

  init()
  return

) jQuery