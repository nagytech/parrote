$ ->

  $('a[data-action=logout').on 'click', () ->
    $('#logout-form').submit()

  $('a[data-action=navigate]').on 'click', (e) ->
    e.preventDefault();
    window.location = $(@).attr('data-action-url')
