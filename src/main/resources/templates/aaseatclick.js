console.log("JavaScript file is being executed.");
$(document).ready(function() {
    $('#seating-container').on('click', '.seat', function() {
        console.log("Seat clicked!"); // Check if this message appears in the console
        $(this).toggleClass('active');
    });
});