
    // Lấy ra form bằng ID đã thêm
    const form = document.getElementById('thanhtoanForm');

    // Lắng nghe sự kiện submit của form ( khi nhấn nút type="submit")
    form.addEventListener('submit', function(event) {

        event.preventDefault();
        if (form.checkValidity()) {
            alert('🎉 Đã đặt hàng thành công!');
        } else {
            form.reportValidity();
        }
    });
