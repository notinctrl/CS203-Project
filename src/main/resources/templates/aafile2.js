$('.cinema-seats .seat').on('click', function() {
    $(this).toggleClass('active');
  });

    // Identify the clicked seat (for example, by getting its unique ID or data attributes)
    // const seatId = $(this).data('seat-id');
    // const seatRow = $(this).data('seat-row');
    // const seatNumber = $(this).data('seat-number');

    // Create an object containing seat data
    // const seatData = {
    //     id: seatId,
    //     row: seatRow,
    //     number: seatNumber,
    // };

    // // Send the seat data to the backend using an HTTP request (e.g., using fetch or AJAX)
    // fetch('/api/book-seat', {
    //     method: 'POST',
    //     headers: {
    //         'Content-Type': 'application/json',
    //     },
    //     body: JSON.stringify(seatData),
    // })
    // .then(response => {
    //     if (response.ok) {
    //         // Handle a successful response (e.g., seat booked successfully)
    //         console.log('Seat booked successfully');
    //     } else {
    //         // Handle errors, if any
    //         console.error('Error booking seat');
    //     }
    // })
    // .catch(error => {
    //     // Handle network or other errors
    //     console.error('Error:', error);
    // });