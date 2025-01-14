
//Function to handle adding a movie to a collection.
function addToCollection(event, form) {
    event.preventDefault(); // Prevent form from submitting normally
    
    const username = document.getElementById('username').value;
    const collectionName = form.collectionName.value;
    const movieTitle = form.movieTitle.value;

    console.log('Adding movie:', {
        username,
        collectionName,
        movieTitle
    });

    if (!collectionName) {
        alert('Please select a collection');
        return false;
    }

    fetch(`/api/collections/${username}/${encodeURIComponent(collectionName)}/add-movie?movieTitle=${encodeURIComponent(movieTitle)}`, {
        method: 'POST'
    })
    .then(response => {
        console.log('Response status:', response.status);
        return response.text();
    })
    .then(message => {
        console.log('Server response:', message);
        alert(message);
        // Show success message
        const successDiv = form.querySelector('.success-message');
        successDiv.textContent = 'Movie added successfully!';
        // Clear any previous error
        form.querySelector('.error-message').textContent = '';
        // Refresh the collection view
        viewCollection();
    })
    .catch(error => {
        console.error('Error:', error);
        // Show error message
        const errorDiv = form.querySelector('.error-message');
        errorDiv.textContent = 'Failed to add movie: ' + error.message;
        // Clear any previous success
        form.querySelector('.success-message').textContent = '';
    });

    return false; // Prevent form submission
}

//Function to view the contents of a specific collection.
function viewCollection() {
    const select = document.getElementById('viewCollectionSelect');
    const collectionName = select.value;
    const username = document.getElementById('username').value;

    console.log('Viewing collection:', {
        collectionName,
        username
    });

    if (!collectionName) {
        alert("Please select a collection to view.");
        return;
    }

    fetch(`/api/collections/${username}/${encodeURIComponent(collectionName)}`)
    .then(response => {
        console.log('Response status:', response.status);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return response.json();
    })
    .then(data => {
        console.log('Collection data:', data);
        const contentElement = document.getElementById('collectionContent');
        if (data && data.movieTitles) {
            if (data.movieTitles.length === 0) {
                contentElement.textContent = 'Movies: No movies in collection';
            } else {
                contentElement.textContent = 'Movies: ' + data.movieTitles.join(', ');
            }
        } else {
            contentElement.textContent = 'Error: Invalid collection data';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('collectionContent').textContent = 
            'Failed to fetch collection: ' + error.message;
    });
}

//Event listener for the "Create Collection" form submission.
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('createCollectionForm').onsubmit = function(e) {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const collectionName = this.collectionName.value;

        fetch(`/api/collections/add?username=${username}&collectionName=${encodeURIComponent(collectionName)}`, {
            method: 'POST'
        })
        .then(response => response.text())
        .then(message => {
            alert(message);
            if (message.includes('successfully')) {
                location.reload();
            }
        })
        .catch(error => {
            alert('Failed to create collection');
        });
    };
});

//Function to show the modal for creating a new collection.
function showModal() {
    document.getElementById('newCollectionModal').style.display = 'block';
}

//Function to hide the modal for creating a new collection.
function closeModal() {
    document.getElementById('newCollectionModal').style.display = 'none';
}
//Close the modal if the user clicks outside of it.
window.onclick = function(event) {
    const modal = document.getElementById('newCollectionModal');
    if (event.target == modal) {
        closeModal();
    }
}


