$ ->
  # Handle the logout action
  $('a[data-action=logout').on 'click', () ->
    $('#logout-form').submit()

  # Navigation overrides
  $('a[data-action=navigate]').on 'click', (e) ->
    e.preventDefault();
    window.location = $(@).attr('data-action-url')
