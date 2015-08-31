(($) ->

  submitBonMot = ->
    text = $('#post-form textarea').val()
    $.ajax
      url: '/api/postmessage' # TODO: Try to get reverse routing working
      data:
        message: text
      type: 'POST'
    .done (data) ->
      return
    .fail (error) ->
      return
    return

  $('#post-form button').on 'click', (e) ->
    e.preventDefault()
    submitBonMot()
    return

  return

) jQuery
