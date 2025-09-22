package ru.practicum.ewm.event.interfaces;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.exceptions.EventStateException;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.exceptions.EventParticipantNotExists;
import ru.practicum.ewm.exception.model.ConflictException;
import ru.practicum.ewm.exception.model.NotExistsException;
import ru.practicum.ewm.exception.model.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.statistics.RequestInfo;

import java.util.List;

public interface EventService {

    /**
     * <h4> Public: /events/{id} </h4>
     * - событие должно быть опубликовано <br>
     * - информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов <br>
     * - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, сохранить в сервисе статистики <br>
     * <br>
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     */
    EventFullDto getPublishedEventById(long id, RequestInfo info) throws EventNotFound;

    /**
     * <h4> Public: /events</h4>
     * - это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события <br>
     * - текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв <br>
     * - если в запросе не указан диапазон дат [rangeStart-rangeEnd],
     * то нужно выгружать события, которые произойдут позже текущей даты и времени <br>
     * - информация о каждом событии должна включать в себя
     * количество просмотров и количество уже одобренных заявок на участие <br>
     * - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики <br>
     * <br>
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     */
    List<EventShortDto> findPublishedEvents(EventFilter filter, RequestInfo info);

    /**
     * <h4> Admin: GET /admin/events </h4>
     * - Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия <br>
     * - В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список <br>
     */
    List<EventFullDto> findAllEvents(EventFilter filter);

    /**
     * <h4> Admin: /events/{eventId} </h4>
     * Редактирование данных любого события администратором. Валидация данных не требуется. Обратите внимание:<br>
     * - дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (код ошибки 409)<br>
     * - событие можно публиковать, только если оно в состоянии ожидания публикации ( код ошибки 409)<br>
     * - событие можно отклонить, только если оно еще не опубликовано (код ошибки 409)<br>
     */
    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest dto, RequestInfo info) throws NotFoundException, EventStateException;

    /**
     * <h4> Private: GET /users/{userId}/events</h4>
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список<br>
     * 400 вернется если @RequestParam в контроллере не конвертируются<br>
     */
    List<EventShortDto> findEventsAddedByUser(Long userId, EventFilter filter);

    /**
     * <h4> Private: POST /users/{userId}/events</h4>
     * дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента<br>
     */
    EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) throws EventStateException, NotFoundException;

    /**
     * <h4> Private: GET /users/{userId}/events/{eventId}</h4>
     * В случае, если события с заданным id не найдено, возвращает статус код 404<br>
     */
    EventFullDto findEvent(
            Long userId,
            Long eventId
    ) throws EventNotFound;

    /**
     * <h4> Private: PATCH /users/{userId}/events/{eventId}</h4>
     * - изменить можно только отмененные события или события в состоянии ожидания модерации (код ошибки 409)<br>
     * - дата и время на которые намечено событие не может быть раньше,
     * чем через два часа от текущего момента (Ожидается код ошибки 409)<br>
     */
    EventFullDto updateEvent(
            Long userId,
            Long eventId,
            UpdateEventUserRequest dto,
            RequestInfo info) throws EventStateException, EventNotFound, NotFoundException, NotExistsException, ConflictException;

    /**
     * <h4> Private: GET /users/{userId}/events/{eventId}/requests</h4>
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список<br>
     */
    List<ParticipationRequestDto> findParticipationRequestInfo(Long userId, Long eventId);

    /**
     * <h4> Private: PATCH /users/{userId}/events/{eventId}/requests</h4>
     * - если для события лимит заявок равен 0 или отключена пре-модерация заявок,
     * то подтверждение заявок не требуется<br>
     * - подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)<br>
     * - статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)<br>
     * - если при подтверждении данной заявки, лимит заявок для события исчерпан,
     * то все неподтверждённые заявки необходимо отклонить<br>
     */
    EventRequestStatusUpdateResult updateParticipationRequestInfo(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest dto) throws EventNotFound, EventParticipantNotExists,NotExistsException;

}