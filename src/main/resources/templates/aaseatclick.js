let selectedSeat = null;
console.log("JavaScript file is being executed.");
$(document).ready(function() {
    $('#seating-container').on('click', '.seat', function() {
        console.log("Seat clicked!"); // Check if this message appears in the console
        $(this).toggleClass('active');
        
        // Get the seat information (e.g., row and seat number)
        const seatInfo = $(this).text(); // Assuming the seat contains text
        selectedSeat = seatInfo; // Store the selected seat
    });
});

// Function to send the selected seat to the backend
function sendSelectedSeatToBackend() {
    if (selectedSeat) {
        fetch('', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ selectedSeat }), // Send the selected seat as JSON data
        })
        .then(response => {
            if (response.ok) {
                console.log('Selected seat sent to backend successfully.');
            } else {
                console.error('Failed to send selected seat to backend.');
            }
        })
        .catch(error => {
            console.error('Error sending selected seat to backend:', error);
        });
    }
}

// Call the function when you want to send the selected seat to the backend (e.g., after a button click)
// sendSelectedSeatToBackend();