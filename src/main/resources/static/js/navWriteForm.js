document.addEventListener('DOMContentLoaded', () => {
    const writeLink = document.getElementById('write-link');
    const currentPath = window.location.pathname;

    if (currentPath.startsWith('/list')) {
        writeLink.href = '/itemInfo/write';
    } else if (currentPath.startsWith('/notice')) {
        writeLink.href = '/noticeInfo/write'
    }

    writeLink.addEventListener('click', (event) => {
        event.preventDefault();
        fetch('/api/check-auth')
            .then(response => {
                if (response.status === 200) {
                    window.location.href = writeLink.href;
                } else {
                    alert('로그인이 필요합니다');
                    window.location.href = '/login';
                }
            })
            .catch(error => {
                console.error('Error : ', error);
            });
    });
});