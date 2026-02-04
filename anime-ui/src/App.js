import React, { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [top50, setTop50] = useState([]);
  const [currentTop10, setCurrentTop10] = useState([]);
  const [activeTab, setActiveTab] = useState("top50");
  const [selectedAnime, setSelectedAnime] = useState(null);

  // Fetch Top 50 Anime
  useEffect(() => {
    fetch("http://localhost:8085/anime/top50")
      .then(res => res.json())
      .then(data => setTop50(data));
  }, []);

  // Fetch Current Season Top 10 Anime
  useEffect(() => {
    fetch("http://localhost:8085/anime/currenttop10")
      .then(res => res.json())
      .then(data => setCurrentTop10(data));
  }, []);

  // Handle card click: toggle selection
  const handleCardClick = (a) => {
    if (selectedAnime && selectedAnime.id === a.id) {
      setSelectedAnime(null); // unselect if clicked again
    } else {
      setSelectedAnime(a);
    }
  };

  // Render each card
  const renderCard = (a) => {
    const bgImage = a.imageUrl || "https://via.placeholder.com/300x400?text=No+Image";
    const isSelected = selectedAnime && selectedAnime.id === a.id;

    return (
      <div
        id={`card-${a.id}`}
        className={`anime-card ${isSelected ? "glow" : ""}`}
        key={a.id}
        style={{
          backgroundImage: `url(${bgImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center"
        }}
        onClick={() => handleCardClick(a)}
      >
        <div className="overlay">
          <h3>{a.animeName}</h3>
          <p>Episodes: {a.noOfEp}</p>
          <p>Rating: ⭐ {a.rating}</p>
          <div className="genres">
            {a.genre?.split(",").map((g, i) => (
              <span className="badge" key={i}>{g}</span>
            ))}
          </div>
        </div>
      </div>
    );
  };

  return (
    <div className="container">
      {/* Tabs */}
      <div className="tabs">
        <button
          className={activeTab === "top50" ? "active" : ""}
          onClick={() => setActiveTab("top50")}
        >
          Top 50 Anime
        </button>
        <button
          className={activeTab === "current" ? "active" : ""}
          onClick={() => setActiveTab("current")}
        >
          Current Season Top 10
        </button>
      </div>

      {/* Grid */}
      <div className="grid">
        {activeTab === "top50"
          ? top50.map(renderCard)
          : currentTop10.map(renderCard)}
      </div>

      {/* Modal */}
      {selectedAnime && (
        <div className="modal">
          <div className="modal-content">
            <h2>{selectedAnime.animeName}</h2>
            <img
              src={selectedAnime.imageUrl}
              alt={selectedAnime.animeName}
              style={{ maxWidth: "200px", borderRadius: "8px" }}
            />
            {/* Render AniList description safely */}
            <p
              dangerouslySetInnerHTML={{
                __html: selectedAnime.description || "No description available"
              }}
            />
            <p>Episodes: {selectedAnime.noOfEp}</p>
            <p>Rating: ⭐ {selectedAnime.rating}</p>
            <div className="genres">
              {selectedAnime.genre?.split(",").map((g, i) => (
                <span className="badge" key={i}>{g}</span>
              ))}
            </div>
            <button className="active" onClick={() => setSelectedAnime(null)}>
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;