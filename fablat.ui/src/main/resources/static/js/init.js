$(document).ready(function(){
  $('.parallax').parallax();
  $('.scrollspy').scrollSpy();

  $('.carousel.carousel-slider').carousel({
    full_width: true,
    padding: 20
  });
  autoplay()
  function autoplay() {
      $('.carousel').carousel('next');
      setTimeout(autoplay, 4500);
  }
});
