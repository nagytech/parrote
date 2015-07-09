$ ->

  calcMoment = () ->
    $('.moment-format').each () ->
      f = $(this)
      f.html(moment(f.attr('data-moment-date'), "ddd MMM DD HH:mm:ss").fromNow())

  setInterval(() ->
    calcMoment()
  , 30000)

  calcMoment()