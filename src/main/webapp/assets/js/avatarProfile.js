
    let cropper;
    const avatarInput = document.getElementById('avatarInput');
    const imageToCrop = document.getElementById('imageToCrop');
    const cropperModal = document.getElementById('cropperModal');

    avatarInput.addEventListener('change', function (e) {
    const files = e.target.files;
    if (files && files.length > 0) {
    const file = files[0];
    const reader = new FileReader();

    reader.onload = function (event) {
    imageToCrop.src = event.target.result;
    cropperModal.style.display = 'flex';

    if (cropper) { cropper.destroy(); }

    cropper = new Cropper(imageToCrop, {
    aspectRatio: 1,
    viewMode: 1,
    dragMode: 'move',
    background: false
});
};
    reader.readAsDataURL(file);
}
    avatarInput.value = '';
});

    function closeCropperModal() {
    cropperModal.style.display = 'none';
    if (cropper) cropper.destroy();
}
    function zoomIn() { if (cropper) cropper.zoom(0.1); }
    function zoomOut() { if (cropper) cropper.zoom(-0.1); }

    function uploadAvatar() {
    if (!cropper) return;

    const btnUpload = document.getElementById('btnUpload');
    btnUpload.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Đang xử lý...';
    btnUpload.disabled = true;

    cropper.getCroppedCanvas({
    width: 300,
    height: 300
}).toBlob(function (blob) {

    const formData = new FormData();
    formData.append('action', 'uploadAvatar');
    formData.append('avatarFile', blob, 'avatar.jpg');

    fetch('profile', {
    method: 'POST',
    body: formData
})
    .then(() => {
    window.location.reload();
})
    .catch(error => {
    alert('Có lỗi xảy ra khi kết nối máy chủ!');
    btnUpload.innerHTML = 'Cập nhật';
    btnUpload.disabled = false;
});

}, 'image/jpeg', 0.8);
}
