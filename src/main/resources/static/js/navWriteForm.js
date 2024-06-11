document.addEventListener('DOMContentLoaded', () => {
    const writeLink = document.getElementById('write-link');
    const currentPath = window.location.pathname;

    if (currentPath.startsWith('/list')) {
        writeLink.href = '/itemInfo/write';

    } else if (currentPath.startsWith('/notice')) {
        writeLink.href = '/noticeInfo/write'

    }
});