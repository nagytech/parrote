$ ->
  console.log "coffee script works"

  $('button[data-action=signup]').on 'click', (e) ->
    e.preventDefault()
    console.log('signup');

  $('button[data-action=login]').on 'click', (e) ->
    e.preventDefault()
    console.log('login');