document.addEventListener('DOMContentLoaded', function () {
    const errorMessageElement = document.getElementById('serverErrorMessage');
    const errorMessage = errorMessageElement ? errorMessageElement.value : '';
    const modal = document.getElementById("errorModal");
    const span = document.getElementsByClassName("close")[0];

    console.log("Error message : ", errorMessage); // 디버깅용 로그

    if (errorMessage && errorMessage.trim() !== '') {
        document.getElementById("errorMessage").innerText = errorMessage;
        showModal();
        console.log("Showing modal");
    }

    span.onclick =  hideModal;

    window.onclick = (event) => {
        if (event.target === modal) {
            hideModal();
        }
    }

    function showModal() {
        modal.style.display = "block";
        setTimeout(() => {
            modal.classList.add('show');
        }, 10);
    }

    function hideModal() {
        modal.classList.remove('show');
        setTimeout(() => {
            modal.style.display = "none";
        }, 300)
    }
});