let selectedFile = null;
let uploadedImageUrl = null;

document.addEventListener('DOMContentLoaded', (event) => {
    const fileInput = document.getElementById('fileInput');
    const form = document.getElementById('itemWriteForm');
    const cancelFileButton = document.getElementById('cancelFileBtn');

    console.log('폼 요소:', form);

    if (!fileInput) console.error('fileInput 요소를 찾을 수 없습니다.');
    if (!form) console.error('form 요소를 찾을 수 없습니다.');
    if (!cancelFileButton) console.error('cancelFileButton 요소를 찾을 수 없습니다.');

    fileInput.addEventListener('change', async function (event) {
        console.log('파일이 선택되었습니다.');
        selectedFile = event.target.files[0];
        if (selectedFile) {
            console.log('선택된 파일:', selectedFile.name);
            document.getElementById('selectedFileName').textContent = "선택된 파일: " + selectedFile.name;
            cancelFileButton.style.display = 'inline';

            // 파일 선택 시 바로 업로드 시도
            const result = await uploadFile(selectedFile);
            if (result.success) {
                console.log('파일 업로드가 완료되었습니다.');
                document.getElementById('imageURL').value = result.url;
                uploadedImageUrl = result.url;
            } else {
                console.error("파일 업로드 중 오류 발생:", result.error);
                alert("파일 업로드에 실패했습니다. 다시 시도해주세요.");
            }
        }
    });

    cancelFileButton.addEventListener('click', async function() {
        console.log('삭제 버튼이 클릭되었습니다.');
        console.log('현재 uploadedImageUrl:', uploadedImageUrl);
        if (uploadedImageUrl) {
            const deleteResult = await deleteFile(uploadedImageUrl);
            if (deleteResult.success) {
                console.log('파일이 성공적으로 삭제되었습니다.');
            } else {
                console.error('파일 삭제 실패:', deleteResult.error);
            }
        } else {
            console.log('삭제할 이미지 URL이 없습니다.');
        }
        resetFileInput();
    });

    form.addEventListener('submit', function(e) {
        console.log('폼 제출이 시작되었습니다.');
        // 폼 데이터 로깅
        const formData = new FormData(this);
        for (let [key, value] of formData.entries()) {
            console.log(key, value);
        }
    });
});

async function uploadFile(file) {
    console.log('uploadFile 함수가 호출되었습니다.');
    let name = encodeURIComponent(file.name);

    console.log('pre-signed URL을 요청합니다.');
    let response = await fetch(`/presigned-url?filename=${name}&type=item`);
    console.log('pre-signed URL 응답:', response);

    if (!response.ok) {
        console.error('pre-signed URL 요청 실패:', response.status, response.statusText);
        return { success: false, error: 'Failed to get pre-signed URL' };
    }

    let url = await response.text();
    console.log('받은 pre-signed URL:', url);

    console.log('S3에 파일 업로드를 시도합니다.');
    let uploadResponse = await fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': file.type
        },
        body: file
    });
    console.log('S3 업로드 응답:', uploadResponse);

    if (!uploadResponse.ok) {
        console.error('S3 업로드 실패:', uploadResponse.status, uploadResponse.statusText);
        return { success: false, error: '이미지 업로드에 실패했습니다.' };
    }

    console.log('이미지 업로드에 성공했습니다.');

    let uploadImageUrl = url.split("?")[0];
    console.log('업로드된 이미지 URL:', uploadImageUrl);

    let uploadImage = document.getElementById('uploadImage');
    uploadImage.src = uploadImageUrl;
    uploadImage.style.display = 'block';
    uploadImage.alt = '업로드된 이미지';

    return { success: true, url: uploadImageUrl };
}

async function deleteFile(fileUrl) {
    console.log('deleteFile 함수가 호출되었습니다. URL:', fileUrl);
    if (!fileUrl) {
        console.error('파일 URL이 제공되지 않았습니다.');
        return { success: false, error: 'File URL is missing' };
    }
    try {
        const csrfToken = document.querySelector('meta[name="_csrf"]').content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

        const response = await fetch(`/delete-image?url=${encodeURIComponent(fileUrl)}`, {
            method: 'DELETE',
            headers: {
                [csrfHeader]: csrfToken
            }
        });

        console.log('Delete request sent. Response:', response);

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`파일 삭제 요청 실패: ${response.status} ${errorText}`);
        }

        const result = await response.text();
        return { success: true, message: result };
    } catch (error) {
        console.error('파일 삭제 중 오류 발생:', error);
        return { success: false, error: error.message };
    }
}

function resetFileInput() {
    document.getElementById('fileInput').value = '';
    document.getElementById('selectedFileName').textContent = '';
    document.getElementById('uploadImage').style.display = 'none';
    document.getElementById('imageURL').value = '';
    document.getElementById('cancelFileBtn').style.display = 'none';
    selectedFile = null;
    uploadedImageUrl = null;
}