// Константы игры
const GAME_WIDTH = 800;
const GAME_HEIGHT = 600;
const GAME_TIME = 60;
const ROAD_SPEED = 5;
const COIN_VALUE = 10;
const MIN_X_POSITION = 20;
const BRAKE_POWER = 0.8;

// Уровни сложности
const difficultySettings = {
    easy: {
        obstacles: 3,
        coins: 10,
        obstacleSpeed: 3,
        scoreMultiplier: 1,
        description: 'Few obstacles, more coins'
    },
    medium: {
        obstacles: 5,
        coins: 8,
        obstacleSpeed: 5,
        scoreMultiplier: 1.5,
        description: 'Balanced gameplay'
    },
    hard: {
        obstacles: 7,
        coins: 6,
        obstacleSpeed: 7,
        scoreMultiplier: 2,
        description: 'Many obstacles, high score multiplier'
    }
};

// Характеристики машин
const carTypes = {
    standard: {
        name: 'Standard Car',
        speed: 5,
        acceleration: 0.5,
        color: '#0000FF', // Синяя
        stabilityFactor: 1.2,
        width: 60,
        height: 30
    },
    sport: {
        name: 'Sport Car',
        speed: 6,
        acceleration: 0.7,
        color: '#FF4500', // Оранжевая
        boost: 1.3,
        energy: 100,
        width: 65,
        height: 28
    },
    super: {
        name: 'Super Car',
        speed: 7,
        acceleration: 1,
        color: '#FFD700', // Желтая
        turboBoost: 1.5,
        nitroCharge: 100,
        width: 70,
        height: 25
    }
};

// Игровые переменные
let canvas, ctx;
let player = {
    x: 100,
    y: GAME_HEIGHT / 2,
    width: 60,
    height: 30,
    type: 'standard',
    speed: 0,
    score: 0,
    coinsCollected: 0
};

let gameTime = GAME_TIME;
let obstacles = [];
let coins = [];
let isGameActive = false;
let roadOffset = 0;
let currentDifficulty = 'easy';

// Инициализация игры
function initGame() {
    canvas = document.getElementById('gameCanvas');
    if (!canvas) {
        console.error('Canvas not found!');
        return;
    }
    
    ctx = canvas.getContext('2d');
    canvas.width = GAME_WIDTH;
    canvas.height = GAME_HEIGHT;

    // Немедленно отрисовываем дорогу
    drawRoad();
    
    // Обработчики добавляем только при старте игры
    document.getElementById('startScreen').style.display = 'block';
}

window.addEventListener('keydown', handleKeyDown);
window.addEventListener('keyup', handleKeyUp);


// Старт игры
function startGame() {
    if (!canvas || !ctx) {
        console.error('Canvas not initialized!');
        return;
    }

    // Скрываем стартовый экран
    const startScreen = document.getElementById('startScreen');
    if (startScreen) {
        startScreen.style.display = 'none';
    }

    // Получаем выбранные настройки
    const carSelect = document.getElementById('carSelect');
    const difficultySelect = document.getElementById('difficultySelect');
    
    if (!carSelect || !difficultySelect) {
        console.error('Select elements not found!');
        return;
    }

    player.type = carSelect.value;
    currentDifficulty = difficultySelect.value;

    // Инициализация игры
    resetGame();
    initGameObjects();

    // Запуск игры
    isGameActive = true;
    gameLoop();
}

// Инициализация игровых объектов
function initGameObjects() {
    const difficulty = difficultySettings[currentDifficulty];
    
    // Инициализация препятствий
    obstacles = Array(difficulty.obstacles).fill(null).map(() => ({
        x: GAME_WIDTH + Math.random() * GAME_WIDTH,
        y: 100 + Math.random() * (GAME_HEIGHT - 200),
        width: 40,
        height: 40
    }));
    
    // Инициализация монет
    coins = Array(difficulty.coins).fill(null).map(() => ({
        x: GAME_WIDTH + Math.random() * GAME_WIDTH,
        y: 100 + Math.random() * (GAME_HEIGHT - 200),
        radius: 10,
        collected: false
    }));
}

// Сброс игры
function resetGame() {
    gameTime = GAME_TIME;
    player.score = 0;
    player.coinsCollected = 0;
    player.x = 100;
    player.y = GAME_HEIGHT / 2;
    updateScoreDisplay();
    updateCoinsDisplay();
    document.getElementById('timeLeft').textContent = GAME_TIME;
}

// Обработка нажатий клавиш

function handleKeyDown(e) {
    if (!isGameActive) return;

    const car = carTypes[player.type];
    switch(e.key) {
        case 'ArrowUp':
            if (player.y > 0) {
                player.y -= car.speed;
            }
            break;
        case 'ArrowDown':
            if (player.y < GAME_HEIGHT - player.height) {
                player.y += car.speed;
            }
            break;
        case 'ArrowLeft':
            if (!turboActive) {
                turboActive = true;
                player.speed = Math.min(player.speed + car.acceleration * 2, MAX_SPEED);
                triggerTurboEffect();
                
                if (turboTimeout) clearTimeout(turboTimeout);
                
                turboTimeout = setTimeout(() => {
                    turboActive = false;
                }, TURBO_DURATION);
            }
            break;
        case ' ': // Space key
            player.speed *= BRAKE_POWER;
            break;
    }
}


function triggerTurboEffect() {
    const turboX = player.x - 10; // Позиция позади автомобиля
    const turboY = player.y + player.height / 2;

    ctx.fillStyle = '#FFA500'; // Оранжевый цвет
    ctx.beginPath();
    ctx.arc(turboX, turboY, 15, 0, Math.PI * 2);
    ctx.fill();

    // Убираем эффект через более длительное время
    setTimeout(() => {
        ctx.clearRect(turboX - 20, turboY - 20, 40, 40);
    }, 300);
}

function handleKeyUp(e) {
    
}

function update() {
    if (!isGameActive) return;

    // Обновление времени
    gameTime -= 1 / 60;
    document.getElementById('timeLeft').textContent = Math.ceil(gameTime);

    if (gameTime <= 0) {
        endGame();
        return;
    }

    // Обновляем положение игрока на основе скорости
    player.x += player.speed;
    if (player.x > GAME_WIDTH - player.width) {
        player.x = GAME_WIDTH - player.width; // Ограничиваем правую границу
    }

    // Движение дороги
    roadOffset = (roadOffset + ROAD_SPEED) % 50;

    const difficulty = difficultySettings[currentDifficulty];

    // Обновление препятствий
    updateObstacles(difficulty);

    // Обновление монет
    updateCoins(difficulty);
}


// Обновление препятствий
function updateObstacles(difficulty) {
    obstacles.forEach(obstacle => {
        obstacle.x -= difficulty.obstacleSpeed;
        
        if (obstacle.x < -obstacle.width) {
            obstacle.x = GAME_WIDTH + Math.random() * 200;
            obstacle.y = 100 + Math.random() * (GAME_HEIGHT - 200);
        }

        if (checkCollision(player, obstacle)) {
            player.score = Math.max(0, player.score - 5 * difficulty.scoreMultiplier);
            showCollisionEffect();
            updateScoreDisplay();
        }
    });
}

// Обновление монет
function updateCoins(difficulty) {
    coins.forEach(coin => {
        if (!coin.collected) {
            coin.x -= difficulty.obstacleSpeed;

            if (coin.x < -coin.radius * 2) {
                coin.x = GAME_WIDTH + Math.random() * 200;
                coin.y = 100 + Math.random() * (GAME_HEIGHT - 200);
            }

            if (checkCoinCollision(player, coin)) {
                collectCoin(coin, difficulty);
            }
        }
    });
}

// Отрисовка
function draw() {
    ctx.clearRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
    drawRoad();
    
    // Отрисовываем игровые объекты только если игра активна
    if (isGameActive) {
        obstacles.forEach(drawObstacle);
        coins.forEach(coin => {
            if (!coin.collected) drawCoin(coin);
        });
        drawPlayer();
    }
}

function drawRoad() {
    // Фон дороги
    ctx.fillStyle = '#333';
    ctx.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

    // Разметка дороги
    ctx.strokeStyle = '#FFF';
    ctx.setLineDash([30, 30]);
    
    // Горизонтальная линия посередине
    const centerY = GAME_HEIGHT / 2;
    
    // Рисуем несколько отрезков для создания пунктирной линии
    for (let x = -roadOffset; x < GAME_WIDTH; x += 100) {
        ctx.beginPath();
        ctx.moveTo(x, centerY);
        ctx.lineTo(x + 50, centerY);
        ctx.stroke();
    }
    ctx.setLineDash([]);
}

function drawPlayer() {
    const car = carTypes[player.type];

    // Рисуем турбо-эффект, если скорость выше базовой
    if (player.speed > car.speed) {
        ctx.fillStyle = 'rgba(255, 165, 0, 0.7)'; // Оранжевый
        ctx.beginPath();
        ctx.arc(player.x - 10, player.y + player.height / 2, 15, 0, Math.PI * 2);
        ctx.fill();
    }

    // Рисуем автомобиль
    ctx.fillStyle = car.color;
    ctx.beginPath();
    ctx.moveTo(player.x, player.y + player.height);
    ctx.lineTo(player.x + player.width, player.y + player.height);
    ctx.lineTo(player.x + player.width, player.y);
    ctx.lineTo(player.x + player.width * 0.7, player.y);
    ctx.lineTo(player.x + player.width * 0.6, player.y - 5);
    ctx.lineTo(player.x + player.width * 0.3, player.y - 5);
    ctx.lineTo(player.x + player.width * 0.2, player.y);
    ctx.lineTo(player.x, player.y);
    ctx.closePath();
    ctx.fill();

    // Рисуем колеса
    ctx.fillStyle = '#000';
    ctx.beginPath();
    ctx.arc(player.x + player.width * 0.2, player.y + player.height, 6, 0, Math.PI * 2);
    ctx.arc(player.x + player.width * 0.8, player.y + player.height, 6, 0, Math.PI * 2);
    ctx.fill();
}



// Отрисовка препятствия
function drawObstacle(obstacle) {
    ctx.fillStyle = '#FF4444';
    ctx.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
}

// Отрисовка монеты
function drawCoin(coin) {
    ctx.beginPath();
    ctx.arc(coin.x, coin.y, coin.radius, 0, Math.PI * 2);
    ctx.fillStyle = '#FFD700';
    ctx.fill();
    ctx.strokeStyle = '#FFA500';
    ctx.stroke();
}

// Проверка столкновений
function checkCollision(a, b) {
    return a.x < b.x + b.width &&
           a.x + a.width > b.x &&
           a.y < b.y + b.height &&
           a.y + a.height > b.y;
}

function checkCoinCollision(player, coin) {
    const dx = (player.x + player.width/2) - coin.x;
    const dy = (player.y + player.height/2) - coin.y;
    return Math.sqrt(dx*dx + dy*dy) < (player.width/2 + coin.radius);
}

function showCollisionEffect() {
    const originalColor = carTypes[player.type].color;
    const canvas = document.getElementById('gameCanvas');

    let blinkCount = 0;
    canvas.classList.add('collision-effect');
    const blinkInterval = setInterval(() => {
        player.blinking = !player.blinking;

        if (++blinkCount > 6) {
            clearInterval(blinkInterval);
            canvas.style.boxShadow = '';
            canvas.classList.remove('collision-effect'); 
        }
    }, 100);
}



function collectCoin(coin, difficulty) {
    coin.collected = true;
    player.score += COIN_VALUE * difficulty.scoreMultiplier;
    player.coinsCollected++;
    updateScoreDisplay();
    updateCoinsDisplay();

    // Перемещаем монету после сбора
    setTimeout(() => {
        coin.collected = false;
        coin.x = GAME_WIDTH + Math.random() * 200;
        coin.y = 100 + Math.random() * (GAME_HEIGHT - 200);
    }, 1000);
}

// Обновление UI
function updateScoreDisplay() {
    document.getElementById('score').textContent = Math.floor(player.score);
}

function updateCoinsDisplay() {
    document.getElementById('coinCount').textContent = player.coinsCollected;
}

// Завершение игры
function endGame() {
    isGameActive = false;
    
    const message = `Game Over!\nFinal Score: ${Math.floor(player.score)}\nCoins Collected: ${player.coinsCollected}`;
    alert(message);
    
    // Сбрасываем игру и показываем стартовый экран
    document.getElementById('startScreen').style.display = 'block';
    document.querySelector('.game-info').style.display = 'none';
    
    // Очищаем обработчики
    document.removeEventListener('keydown', handleKeyDown);
    document.removeEventListener('keyup', handleKeyUp);
    
    // Отрисовываем пустую дорогу
    draw();
}

// Игровой цикл
function gameLoop() {
    if (isGameActive) {
        update();
        draw();
        requestAnimationFrame(gameLoop);
    }
}

// Инициализация при загрузке страницы
window.onload = initGame;