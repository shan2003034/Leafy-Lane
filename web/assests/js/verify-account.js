async function verifyUserAccount() {

    const vCode = document.getElementById("verificationCode").value;

    const verification = {
        vCode: vCode
    };

    const verificationJson = JSON.stringify(verification);

    const response = await fetch(
            "VerifyAccount",
            {
                method: "POST",
                body: verificationJson,
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        console.log("ok");
        const json = await response.json();
        console.log(json);
        if (json.status) {
            console.log(true);
            window.location = "index.html";
        } else {
            console.log(false);
            if (json.message == "ENF") {
                window.location = "login.html";
            } else {
                document.getElementById("message").innerHTML = "Verification failde.pleade try again later";
            }
        }

    } else {
    }
}

