$(document).ready(function(){
	
  $('.parallax').parallax();
  $('.scrollspy').scrollSpy();
  //Initialize collapse button
  $(".nav-menu-button-collapse").sideNav();

  $('.carousel.carousel-slider').carousel({
    full_width: true,
    padding: 20
  });
  autoplay();
  function autoplay() {
      $('.carousel').carousel('next');
      setTimeout(autoplay, 4500);
  }
  
});