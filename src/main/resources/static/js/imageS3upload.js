document.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById('fileInput').addEventListener('change', getURL)
});

async function getURL(event) {
    let file = event.target.files[0];
    if (!file) {
        console.error('No file selected');

        return;
    }

    let name = encodeURIComponent(file.name);

    try {
        let response = await fetch('/presigned-url?filename=' + name);
        if (!response.ok) throw new Error('Failed to get pre-signed URL');

        let url = await response.text();

        let uploadResponse = await fetch(url, {
           method: 'PUT',
           headers: {
               'Content-Type': file.type
           },
           body: file
        });

        if (!uploadResponse.ok) throw new Error('Failed to upload file');

        console.log('File uploaded successfully');

        let uploadedImageUrl = url.split("?")[0];
        console.log(uploadedImageUrl);

        if (uploadResponse.ok) {
            document.getElementById('uploadImage').src = uploadedImageUrl;
            document.getElementById("imageURL").value = uploadedImageUrl;
        }

    } catch (error) {
        console.error(error)
    }
}