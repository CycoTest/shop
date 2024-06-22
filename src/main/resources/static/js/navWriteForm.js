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
                if (confirm('로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?')) {
                    window.location.href = '/login';

                } else {
                    window.location.href = '/list';
                }
            }
        })
        .catch(error => {
            console.error('Error : ', error);
        });
    });
});