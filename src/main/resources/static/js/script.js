// LOGIN PAGE
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

// SIGN UP PAGE
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

// HEADER

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

// GAMES PAGE

const search = document.getElementById("search");
const navSearch = document.getElementById("navSearch");

const params = new URLSearchParams(window.location.search);

const fetchGames = async () => {
  const gamesContainer = document.getElementById("gamesContainer");

  try {
    const params = new URLSearchParams(window.location.search);

    const response = await fetch(`/api/games?${params}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const games = await response.json();

    gamesContainer.innerHTML = "";

    if (games.length) {
      games.forEach((game) => {
        const gameElement = document.createElement("div");
        gameElement.classList.add("game");
        const imgContainer = document.createElement("div");
        imgContainer.classList.add("gameImg");
        const img = document.createElement("img");
        img.src = `uploads/${game.img}`;
        img.alt = "game img";
        imgContainer.appendChild(img);
        gameElement.appendChild(imgContainer);
        const gameInfo = document.createElement("div");
        gameInfo.classList.add("gameInfo");
        gameInfo.innerHTML = `
  <h3>${game.title}</h3>
  
  <div><span class="genre">${game.genre}</span><span class="rating"
    >${game.rating}<svg
      width="12"
      height="12"
      viewBox="0 0 100 100"
      xmlns="http://www.w3.org/2000/svg"
    >
      <polygon
        points="50,5 61,39 98,39 67,61 78,95 50,75 22,95 33,61 2,39 39,39"
        fill="#efb036"
      />
    </svg>
  </span></div>
  <p>${game.description}</p>
  <span class="price">${game.price}$</span>
  <button type="button">Buy Game</button>
          `;
        gameElement.appendChild(gameInfo);
        gamesContainer.appendChild(gameElement);
      });
    } else {
      gamesContainer.innerHTML =
        "<span class='no-games'> No Game Found </span>";
    }
  } catch (error) {
    console.log(error);
  }
};

const fetchGenres = async () => {
  const genresContainer = document.getElementById("genres");

  try {
    const response = await fetch(`/api/games/genres`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const genres = await response.json();

    if (genres.length) {
      const params = new URLSearchParams(window.location.search);
      genres.forEach((genre) => {
        const genreDiv = document.createElement("button");
        genreDiv.classList.add("genre");
        genreDiv.id = genre;
        genreDiv.innerText = genre;
        genresContainer.appendChild(genreDiv);
        genreDiv.addEventListener("click", async (e) => {
          const genresElements = document.querySelectorAll(".genre");
          genresElements.forEach((el) => {
            if (el.id === genre) {
              el.classList.contains("active")
                ? el.classList.remove("active")
                : el.classList.add("active");
            } else if (el.classList.contains("active")) {
              el.classList.remove("active");
            }
          });
          if (params.get("genre") === genre) {
            params.delete("genre");
          } else {
            params.set("genre", genre);
          }
          window.history.replaceState(
            null,
            "",
            `?${params}`
          );
          try {
            await fetchGames();
          } catch (error) {
            console.log(error.message);
          }
        });
      });
    }
  } catch (error) {
    console.log(error);
  }
};

search.addEventListener("keydown", async (e) => {
  if (e.key === "Enter") {
    e.preventDefault();
    const params = new URLSearchParams(window.location.search);
    params.set("search", search.value.trim());
    window.location.pathname === "/games.html"
      ? window.history.replaceState(
          null,
          "",
          `?${params}`
        )
      : (window.location.href = `/games.html?${params}`);

    navSearch.value = params.get("search");
    try {
      await fetchGames();
    } catch (error) {
      console.log(error.message);
    }
  }
});

navSearch.addEventListener("keydown", async (e) => {
  if (e.key === "Enter") {
    e.preventDefault();
    const params = new URLSearchParams(window.location.search);
    params.set("search", navSearch.value.trim());

    window.location.pathname === "/games.html"
      ? window.history.replaceState(
          null,
          "",
          `?${params}`
        )
      : (window.location.href = `/games.html?${params}`);

    search.value = params.get("search");

    try {
      await fetchGames();
    } catch (error) {
      console.log(error.message);
    }
  }
});

if (window.location.pathname === "/games.html") {
  search.value = params.get("search");
  navSearch.value = params.get("search");

  document.addEventListener("DOMContentLoaded", async (e) => {
    e.preventDefault();

    try {
      await fetchGenres();
      await fetchGames();

      const activeGenre = document.getElementById(params.get("genre"));

      if (activeGenre) {
        activeGenre.classList.add("active");
      }
    } catch (error) {
      console.log(error.message);
    }
  });

  const genresContainer = document.getElementById("genres");
  genresContainer.addEventListener("click", (e) => {
    const clicked = e.target.closest(".genre");

    if (!clicked || !genresContainer.contains(clicked)) return;

    const containerRect = genresContainer.getBoundingClientRect();

    const isLeftClick =
      e.clientX < containerRect.left + containerRect.width / 2;

    const offsetLeft = clicked.offsetLeft - 50;
    const offsetRight =
      offsetLeft - genresContainer.clientWidth + clicked.offsetWidth;

    genresContainer.scrollTo({
      left: isLeftClick ? offsetRight : offsetLeft,
      behavior: "smooth",
    });
  });
}
