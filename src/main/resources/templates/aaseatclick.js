$(document).ready(function() {
    $('.cinema-seats .seat').on('click', function() {
        console.log("we are meeming");
        $(this).toggleClass('active');
    });
});