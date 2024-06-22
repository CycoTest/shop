document.addEventListener('DOMContentLoaded', () => {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-links a');

    navLinks.forEach(link => {
        const linkPath = link.getAttribute('href')
        if (linkPath === currentPath ||
            (currentPath.includes('/itemInfo/write') && link.id === 'write-link') ||
            (currentPath.includes('/noticeInfo/write') && link.id === 'write-link')) {
            link.classList.add('active')
        } else {
            link.classList.remove('active')
        }
    });
});