$ ->

  $('button[data-action=navigate]').on 'click', () ->
    window.location = $(@).attr('data-action-url')