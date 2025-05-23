const myGamesBtn = document.getElementById("myGamesBtn");
if (myGamesBtn) {
  myGamesBtn.addEventListener("click", () => {
    window.location.href = "/purchase-log";
  });
}

const gamesBtn = document.getElementById("gamesBtn");
if (gamesBtn) {
  gamesBtn.addEventListener("click", () => {
    window.location.href = "/games";
  });
}

// LOGIN PAGE
if (window.location.pathname === "/login") {
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
        localStorage.setItem(
          "u",
          JSON.stringify({ name: data.name, balance: data.balance })
        );
        window.location.href = "/";
      }
    } catch (error) {
      console.log(error.message);
    }
  });
}

// SIGN UP PAGE
if (window.location.pathname === "/signup") {
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
          localStorage.setItem(
            "u",
            JSON.stringify({ name: data.name, balance: data.balance })
          );
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
const loginBtnContainer = document.getElementById("loginBtnContainer");
const loginBtn = document.getElementById("loginBtn");

const balanceContainer = document.getElementById("balanceContainer");

if (loginBtnContainer) {
  loginBtn.addEventListener("click", () => {
    window.location.href = "/login";
  });
}

const value = JSON.parse(localStorage.getItem("u"));

if (!value && (profile || balanceContainer)) {
  balanceContainer.classList.add(".hide")
} else if (loginBtnContainer && value) {
  loginBtnContainer.classList.add(".hide")
}

if (balance && value) {
  balance.innerText = value?.balance || 0;
}

if (profile && value) {
  profile.addEventListener("click", () => {
    window.location.href = "/profile";
  });
  profile.innerText = value.name
    .split(" ")
    .map((str) => str[0])
    .join("");
}

// if (
//   (window.location.href === "/login" || window.location.href === "/signup") &&
//   !value
// ) {
//   window.location.href = "/";
// }

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
        gameElement.addEventListener("click", (e) => {
          if (e.target.closest(".buyBtn")) {
            return;
          }
          window.location.href = `/games/${game.id.toString()}`;
        });
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
  <button type="button" id="buyBtn">Buy Game</button>
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
          window.history.replaceState(null, "", `?${params}`);
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

if (search) {
  search.addEventListener("keydown", async (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      const params = new URLSearchParams(window.location.search);
      params.set("search", search.value.trim());
      window.location.pathname === "/games"
        ? window.history.replaceState(null, "", `?${params}`)
        : (window.location.href = `/games?${params}`);

      navSearch.value = params.get("search");
      try {
        await fetchGames();
      } catch (error) {
        console.log(error.message);
      }
    }
  });
}

if (navSearch) {
  navSearch.addEventListener("keydown", async (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      const params = new URLSearchParams(window.location.search);
      params.set("search", navSearch.value.trim());

      window.location.pathname === "/games"
        ? window.history.replaceState(null, "", `?${params}`)
        : (window.location.href = `/games?${params}`);

      search.value = params.get("search");

      try {
        await fetchGames();
      } catch (error) {
        console.log(error.message);
      }
    }
  });
}

if (window.location.pathname === "/games") {
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

// GAME PAGE

const fetchGame = async ({ id }) => {
  const gameContainer = document.getElementById("gameContainer");

  try {
    const response = await fetch(`/api/games/${id}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message);
    }

    const game = await response.json();

    document.title = game.title || "Games";

    const imgContainer = document.createElement("div");
    imgContainer.classList.add("gameImg");
    imgContainer.style.backgroundImage = `url('../uploads/${game.img}')`;
    const infoContainer = document.createElement("div");
    infoContainer.classList.add("infoContainer");
    infoContainer.innerHTML = `
        <h1 class="title">${game.title}</h1>
        <span class="genre">${game.genre}</span>
        <p class="desc">${game.description}</p>
        `;

    const sysRequirements = document.createElement("div");
    sysRequirements.classList.add("sysRequirements");
    sysRequirements.innerHTML = "<h3>System requirements</h3>";
    const list = document.createElement("ul");
    Object.entries(game.system_requirements).map(([key, value]) => {
      const li = document.createElement("li");
      li.innerHTML = `${
        key === "os" ? key.toUpperCase() : key
      } : <span>${value}</span>`;
      list.appendChild(li);
    });

    const ratingContainer = document.createElement("div");
    ratingContainer.classList.add("ratingContainer");
    ratingContainer.innerHTML = `
    <h3>Rating : </h3>
    <span id="rating"
    > ${game.rating} <svg
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
  </span>
  <span class="count">( <span id="count">${game.rating_count}</span> rating )</span>
  `;

    const rate = document.createElement("div");
    rate.classList.add("rateBtns");
    let rating = game.rating || 0;
    Array.from({ length: 5 }, (_, i) => i + 1).forEach((value, index) => {
      const btn = document.createElement("button");
      btn.addEventListener("click", async (e) => {
        try {
          if (!rating) {
            await fetch(`/api/games/${id}/rate`, {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({ rating }),
            });

            rating = index + 1;
            for (let i = 0; i <= index; i++) {
              document.getElementById(`star${i}`).style.fill = "#efb036";
            }
            document.getElementById("rating").innerText =
              (game.rating * game.rating_count + rating) /
              (game.rating_count + 1);

            document.getElementById("count").innerText = game.rating_count + 1;
          } else {
            return;
          }
        } catch (error) {
          console.log(error.message);
        }
      });

      btn.type = "button";
      btn.innerHTML = `
          <svg
            width="14"
            height="14"
            viewBox="0 0 100 100"
            xmlns="http://www.w3.org/2000/svg"
          >
            <polygon
            id="star${index}"
              points="50,5 61,39 98,39 67,61 78,95 50,75 22,95 33,61 2,39 39,39"
              fill="#${index < rating ? "efb036" : "f5f5f580"}"
            />
          </svg>`;

      rate.appendChild(btn);
    });

    ratingContainer.appendChild(rate);

    const buyContainer = document.createElement("div");
    buyContainer.classList.add("buyContainer");
    buyContainer.innerHTML = `
      <p>${game.price}$</p>
      <p id="msg"></p>
    `;

    const buyBtn = document.createElement("button");
    buyBtn.type = "button";
    buyBtn.id = "buyBtn";
    buyBtn.innerHTML = "Buy Game";
    buyContainer.appendChild(buyBtn);
    buyBtn.addEventListener("click", async () => {
      try {
        const response = await fetch(`/api/games/${id}/purchase`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
        });

        if (response.ok) {
          const successMsg = document.getElementById("msg");
          successMsg.classList.add("success");
          successMsg.innerText = "Game purchased successfully";
          setTimeout(() => {
            successMsg.innerText = "";
          }, 3000);
        } else if (response.status === 409) {
          const errorData = await response.json();
          const errorMsg = document.getElementById("msg");
          errorMsg.classList.add("error");
          errorMsg.innerText = errorData.message;
          setTimeout(() => {
            errorMsg.innerText = "";
          }, 3000);
          return;
        } else if (response.status === 400) {
          const errorData = await response.json();
          const errorMsg = document.getElementById("msg");
          errorMsg.classList.add("error");
          errorMsg.innerText = errorData.message;
          setTimeout(() => {
            errorMsg.innerText = "";
          }, 3000);
          return;
        } else {
          const errorData = await response.json();
          throw new Error(errorData.message);
        }
      } catch (error) {
        console.log(error);
      }
    });

    sysRequirements.appendChild(list);
    infoContainer.appendChild(sysRequirements);
    infoContainer.appendChild(ratingContainer);
    infoContainer.appendChild(buyContainer);
    gameContainer.appendChild(imgContainer);
    gameContainer.appendChild(infoContainer);
  } catch (error) {
    console.log(error);
  }
};

if (window.location.pathname.startsWith("/games/")) {
  document.addEventListener("DOMContentLoaded", async (e) => {
    e.preventDefault();

    const pathname = window.location.pathname;

    try {
      await fetchGame({ id: pathname.split("/")[2] });
    } catch (error) {
      console.log(error.message);
    }
  });
}

// PROFILE PAGE

const fetchProfile = async () => {
  try {
    const response = await fetch(`/api/users/me`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });
    const data = await response.json();
    if (response.ok) {
      const name = document.getElementById("name");
      const email = document.getElementById("email");
      name.innerText = data?.name;
      email.innerText = data?.email;
    } else {
      window.location.href = "/";
    }
  } catch (error) {
    console.log(error);
  }
};

const logout = async () => {
  try {
    const response = await fetch(`/api/auth/logout`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });
    const data = await response.json();
    if (response.ok) {
      localStorage.removeItem("u");
      window.location.href = "/";
    } else {
      throw new Error(data.message);
    }
  } catch (error) {
    console.log(error);
  }
};

const deleteAccount = async () => {
  try {
    const response = await fetch(`/api/users`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });
    const data = await response.json();
    if (response.ok) {
      localStorage.removeItem("u");
      window.location.href = "/";
    } else {
      throw new Error(data.message);
    }
  } catch (error) {
    console.log(error);
  }
};

if (window.location.pathname === "/profile") {
  document.addEventListener("DOMContentLoaded", async (e) => {
    e.preventDefault();
    try {
      await fetchProfile();
    } catch (error) {
      throw new Error(error.message);
    }
  });

  const logoutBtn = document.getElementById("logout");
  logoutBtn.addEventListener("click", async () => {
    try {
      await logout();
    } catch (error) {
      console.log(error.message);
    }
  });

  const deleteBtn = document.getElementById("delete");
  deleteBtn.addEventListener("click", async () => {
    try {
      await deleteAccount();
    } catch (error) {
      console.log(error.message);
    }
  });
}

// PURCHASE LOG PAGE

const fetchPurchaseLog = async () => {
  try {
    const response = await fetch(`/api/users/purchases`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const logs = await response.json();
    const logsContainer = document.getElementById("logsContainer");
    if (response.ok && logs.length) {
      logs.forEach((log) => {
        const logContainer = document.createElement("div");
        logContainer.classList.add("log");
        logContainer.innerHTML = `
        <div class="gameImg">
        <img src="${`uploads/${log?.game?.img}`}" alt="game img" />
        </div>
        <div class="logInfo">
        <h3>${log?.game?.title}</h3>
        <div class="price"><span>Price :</span> ${log?.game?.price}$</</div>
        <div class="date"><span>Date :</span> ${new Date(log?.createdAt)
          .toLocaleDateString()
          .replace(/\//g, ".")}</</div>
        <a href="/games/${log?.game?.id}">View Game</a>
        </div>
        `;
        logsContainer.appendChild(logContainer);
      });
    } else if (response.status == 401) {
      const loginBtn = document.createElement("button");
      loginBtn.type = "button";
      loginBtn.classList.add("loginBtn");
      loginBtn.innerText = "Log in";
      loginBtn.addEventListener("click", () => {
        window.location.href = "/login";
      });
      logsContainer.appendChild(loginBtn);
    } else if (!logs.length) {
      const gamesBtn = document.createElement("button");
      const note = document.createElement("span");
      note.classList.add("note");
      note.innerText = "Not games yet";
      gamesBtn.type = "button";
      gamesBtn.classList.add("gamesBtn");
      gamesBtn.innerText = "Add Games";
      gamesBtn.addEventListener("click", () => {
        window.location.href = "/games";
      });
      logsContainer.appendChild(note);
      logsContainer.appendChild(gamesBtn);
    } else {
      throw new Error(data.message);
    }
  } catch (error) {
    console.log(error);
  }
};

if (window.location.pathname === "/purchase-log") {
  document.addEventListener("DOMContentLoaded", async (e) => {
    e.preventDefault();

    try {
      await fetchPurchaseLog();
    } catch (error) {
      console.log(error.message);
    }
  });
}
