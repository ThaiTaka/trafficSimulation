-- Tao bang luu thong tin cac mau xe
CREATE TABLE VehicleModels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT UNIQUE NOT NULL,
    weight REAL NOT NULL,  -- Khoi luong (kg)
    speed REAL NOT NULL,   -- Van toc mac dinh (km/h)
    passengers INTEGER NOT NULL, -- So nguoi tren xe
    safetyFactor REAL NOT NULL, -- He so an toan
    hardness REAL NOT NULL -- Do cung
);

-- Chen du lieu mau xe
INSERT INTO VehicleModels (name, weight, speed, passengers, safetyFactor, hardness) VALUES
('Ford', 1800, 80, 5, 0.85, 1.3),
('Kia', 1500, 70, 4, 0.8, 1.2),
('Hyundai', 1600, 75, 4, 0.82, 1.25),
('Raptor', 2200, 90, 5, 0.9, 1.5);
