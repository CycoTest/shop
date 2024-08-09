document.addEventListener('DOMContentLoaded', function () {
    const starRating = document.querySelector('.star-rating');
    const stars = starRating.querySelectorAll('.star');
    const ratingInput = document.getElementById('ratingInput');
    const ratingValue = document.querySelector('.rating-value');

    stars.forEach(star => {
        star.addEventListener('click', setRating);
        star.addEventListener('mouseover', hoverRating);
        star.addEventListener('mouseout', resetRating);
    });

    function setRating(e) {
        const value = e.target.getAttribute('data-value');
        ratingInput.value = value;
        updateStars(value);
        ratingValue.textContent = value + '5.0';
    }

    function hoverRating(e) {
        const value = e.target.getAttribute('data-value');
        updateStars(value);
    }

    function resetRating() {
        const currentRating = ratingInput.value || 0;
        updateStars(currentRating);
    }

    function updateStars(value) {
        stars.forEach(star => {
            const starValue = parseFloat(star.getAttribute('data-value'));
            if (starValue <= value) {
                star.classList.add('active');
                if (starValue + 0.5 <= value) {
                    star.classList.add('full');
                } else {
                    star.classList.remove('full');
                }
            } else {
                star.classList.remove('active', 'full');
            }
        });
    }
});