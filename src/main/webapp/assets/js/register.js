
    const form = document.getElementById('registerForm');

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        if (form.checkValidity()) {
            alert('🎉 Đăng ký thành công! Vui lòng Đăng nhập.');
        } else {
            form.reportValidity();
        }
    });
