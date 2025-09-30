package ru.practicum.ewm.event.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import jakarta.annotation.Generated;
import ru.practicum.ewm.category.model.QCategory;
import ru.practicum.ewm.enums.State;
import ru.practicum.ewm.user.model.QUser;

import java.time.LocalDateTime;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvent extends EntityPathBase<Event> {

    private static final long serialVersionUID = -299444598L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEvent event = new QEvent("event");

    public final StringPath annotation = createString("annotation");

    public final QCategory category;

    public final NumberPath<Integer> confirmedRequests = createNumber("confirmedRequests", Integer.class);

    public final DateTimePath<LocalDateTime> createdOn = createDateTime("createdOn", LocalDateTime.class);

    public final StringPath description = createString("description");

    public final DateTimePath<LocalDateTime> eventDate = createDateTime("eventDate", LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser initiator;

    public final QLocation location;

    public final BooleanPath paid = createBoolean("paid");

    public final NumberPath<Integer> participantLimit = createNumber("participantLimit", Integer.class);

    public final DateTimePath<LocalDateTime> publishedOn = createDateTime("publishedOn", LocalDateTime.class);

    public final BooleanPath requestModeration = createBoolean("requestModeration");

    public final EnumPath<State> state = createEnum("state", State.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QEvent(String variable) {
        this(Event.class, forVariable(variable), INITS);
    }

    public QEvent(Path<? extends Event> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvent(PathMetadata metadata, PathInits inits) {
        this(Event.class, metadata, inits);
    }

    public QEvent(Class<? extends Event> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category")) : null;
        this.initiator = inits.isInitialized("initiator") ? new QUser(forProperty("initiator")) : null;
        this.location = inits.isInitialized("location") ? new QLocation(forProperty("location")) : null;
    }
}
