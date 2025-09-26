package ru.practicum.ewm.enums;

public enum State {
    PENDING, // в ожидании
    PUBLISHED, // опубликовано
    CANCELED, // отменено
    CONFIRMED, // подтверждено
    REJECTED, // отклонено
    SEND_TO_REVIEW, // отправить на просмотр
    CANCEL_REVIEW, // отменить просмотр
    PUBLISH_EVENT, // опубликовать событие
    REJECT_EVENT // отклонить событие
}
