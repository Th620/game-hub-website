if (window.location.pathname === "/login.html") {
  document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
      const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (response.ok) {
        localStorage.setItem("u", JSON.stringify({ name: data.name }));
        window.location.href = "/";
      }
    } catch (error) {
      console.log(error.message);
    }
  });
}

if (window.location.pathname === "/signup.html") {
  document
    .getElementById("signupForm")
    .addEventListener("submit", async (e) => {
      e.preventDefault();

      const name = document.getElementById("name").value;
      const email = document.getElementById("email").value;
      const password = document.getElementById("password").value;

      try {
        const response = await fetch("/api/auth/signup", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ name, email, password }),
        });

        const data = await response.json();

        if (response.ok) {
          localStorage.setItem("u", JSON.stringify({ name: data.name }));
          window.location.href = "/";
        }
      } catch (error) {
        console.log(error);
      }
    });
}

if (
  window.location.pathname === "/" ||
  window.location.pathname === "/index.html"
) {
  const menu = document.getElementById("menu");
  const profile = document.getElementById("profile");
  const balance = document.getElementById("balance");

  balance.innerText = "1200";

  const value = JSON.parse(localStorage.getItem("u"));

  profile.innerText = value.name
    .split(" ")
    .map((str) => str[0])
    .join("");

  profile.addEventListener("click", () => {
    if (menu.classList.contains("hide")) {
      menu.classList.remove("hide");
    } else {
      menu.classList.add("hide");
    }
  });
}
