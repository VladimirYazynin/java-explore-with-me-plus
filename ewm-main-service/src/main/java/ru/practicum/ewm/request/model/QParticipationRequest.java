package ru.practicum.ewm.request.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import jakarta.annotation.Generated;
import ru.practicum.ewm.enums.State;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.user.model.QUser;

import java.time.LocalDateTime;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParticipationRequest extends EntityPathBase<ParticipationRequest> {

    private static final long serialVersionUID = 1478225225L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QParticipationRequest participationRequest = new QParticipationRequest("participationRequest");

    public final DateTimePath<LocalDateTime> created = createDateTime("created", LocalDateTime.class);

    public final QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser requester;

    public final EnumPath<State> status = createEnum("status", State.class);

    public QParticipationRequest(String variable) {
        this(ParticipationRequest.class, forVariable(variable), INITS);
    }

    public QParticipationRequest(Path<? extends ParticipationRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QParticipationRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QParticipationRequest(PathMetadata metadata, PathInits inits) {
        this(ParticipationRequest.class, metadata, inits);
    }

    public QParticipationRequest(Class<? extends ParticipationRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new QEvent(forProperty("event"), inits.get("event")) : null;
        this.requester = inits.isInitialized("requester") ? new QUser(forProperty("requester")) : null;
    }
}
