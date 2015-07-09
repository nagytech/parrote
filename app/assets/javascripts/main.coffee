$ ->

  $('a[data-action=logout').on 'click', () ->
    $('#logout-form').submit()

  $('a[data-action=navigate]').on 'click', () ->
    window.location = $(@).attr('data-action-url')
