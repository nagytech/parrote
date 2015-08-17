$ ->
  # Convert piths within bonmots to clickable links that
  # navigate to the search page for that pith
  linkHash = () ->
    $('.bonmot .words').each (i, v) ->
      f = $(v)
      f.html(f.text()
        .replace(/#(\S*)/g, '<a href="/search?q=%23$1">#$1</a>')
        .replace(/@(\S*)/g, '<a href="/user/$1">@$1</a>'))

  # Convert timestamps into 'n Xs ago' format
  calcMoment = () ->
    $('.moment-format').each (i, v) ->
      f = $(v)
      f.html(moment(f.attr('data-moment-date'), "ddd MMM DD HH:mm:ss").fromNow())

  # Refresh the timestamps every minute
  setInterval(() ->
    calcMoment()
  , 60000)

  # On page load, perform text formatting
  linkHash()
  calcMoment()
