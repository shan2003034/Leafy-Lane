

async function signout() {
    
    const response = await fetch("SignOut");

    if (response.ok) {

        const json = await response.json();
        

        if (json.status) {
            window.location = "login.html";
        } else {
            window.location.reload();
        }

    } else {
        Swal.fire({
                position: 'top-end',
                icon: 'error',
                title: 'Logout failed!',
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                toast: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer);
                    toast.addEventListener('mouseleave', Swal.resumeTimer);
                }
            });
       
    }

}
