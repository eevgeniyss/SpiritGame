# 🐾 Spirit Runner

2D-аркада, созданная на LibGDX. Игрок управляет псом, который бегает, прыгает, преодолевает препятствия и сражается за выживание. Игра поддерживает покадровую анимацию, настраиваемую сложность и локализацию.

---

## 🎮 Особенности

- ✨ Покадровая анимация (бег, прыжок, смерть)
- ☁️ Динамичные облака и декорации
- 🎵 Звук и музыка с возможностью отключения
- ⚙️ Экран настроек (звук, музыка, язык, сложность)
- 🔁 Экран Game Over с перезапуском и выходом
- 🌐 Поддержка языков: русский и английский

---

## 🛠️ Технологии

- [LibGDX](https://libgdx.com/) (Java game framework)
- TextureAtlas / Animation<TextureRegion>
- ShapeRenderer
- Preferences API (сохранение настроек)

---

## 📁 Структура проекта

core/
├── ru.samsung.gamestudio/
│ ├── SpiritGame.java // главный класс
│ ├── screens/
│ │ ├── MainMenuScreen.java
│ │ ├── GameScreen.java
│ │ ├── SettingsScreen.java
│ │ └── GameOverScreen.java
│ └── utils/
│ └── SettingsManager.java // работа с Preferences
assets/
├── dog_run.png, dog_jump.png, dog_dead.png
├── music.mp3, jump.mp3, hit.mp3
├── backsettingsMenu.png, menuplashka.png
├── activity.png, standart.png (чекбоксы)


---

## 🚀 Запуск

1. Клонируй репозиторий
2. Импортируй в IntelliJ IDEA или Android Studio как Gradle проект
3. Убедись, что рабочий модуль — core
4. Нажми ▶ для запуска desktop-версии

---

## 🔧 Управление

| Действие        | Клавиша / Управление |
|-----------------|----------------------|
| Прыжок          | Пробел               |
| Перезапуск      | Кнопка на экране     |
| Выход           | Кнопка на экране     |
| Настройки       | Меню → Settings      |

---

## 📄 Лицензия

MIT License. Используй, изменяй и распространяй свободно.

---

## 🙌 Благодарности

- LibGDX team
- Samsung Innovation Campus
