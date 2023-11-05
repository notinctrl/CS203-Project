// var countDownTime = localStorage.getItem('countDownTime');

// if (countDownTime) {
// countDownTime = new Date(parseInt(countDownTime, 10));

// var x = setInterval(function () {
//     var now = new Date().getTime();
//     var distance = countDownTime - now;

//     var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
//     var seconds = Math.floor((distance % (1000 * 60)) / 1000);

//     document.getElementById("globaltimer").innerHTML = minutes + "m " + seconds + "s ";

//     if (distance < 0) {
//     clearInterval(x);
//     document.getElementById("demo").innerHTML = "EXPIRED";
//     }
// }, 1000);
// }

// Set the date we're counting down to
var countDownTime = new Date();
countDownTime.setMinutes(countDownTime.getMinutes() + 10);

// Store the countdown time in localStorage
localStorage.setItem('countDownTime', countDownTime.getTime());

// Floating timer element
var floatingTimer = document.getElementById('floating-timer');

// Function to update the floating timer position
function updateFloatingTimerPosition() {
//   if (window.scrollY > 100) {
//     // Show and position the timer when the user scrolls down (adjust 100 as needed)
    floatingTimer.style.display = 'block';
//   } else {
    // Hide the timer when the user is at the top of the page
    // floatingTimer.style.display = 'none';
//   }
}

// Add a scroll event listener to update the timer position
window.addEventListener('scroll', updateFloatingTimerPosition);

// Update the countdown timer
var x = setInterval(function () {
  var now = new Date().getTime();
  var distance = countDownTime - now;

  var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
  var seconds = Math.floor((distance % (1000 * 60)) / 1000);

  document.getElementById('global-timer').innerHTML = minutes + 'm ' + seconds + 's ';

  if (distance < 0) {
    clearInterval(x);
    document.getElementById('global-timer').innerHTML = 'EXPIRED';
  }
}, 1000);
