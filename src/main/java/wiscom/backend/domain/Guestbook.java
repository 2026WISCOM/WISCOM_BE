package wiscom.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wiscom.backend.apiPayload.code.status.ErrorStatus;
import wiscom.backend.apiPayload.exception.handler.GuestbookHandler;
import wiscom.backend.converter.TeamIdAttributeConverter;
import wiscom.backend.domain.common.BaseEntity;
import wiscom.backend.domain.enums.TeamId;

@Getter
@Entity
@Table(name = "guestbook", indexes = @Index(name = "idx_guestbook_team_created", columnList = "team_id, created_at"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guestbook extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = TeamIdAttributeConverter.class)
    @Column(name = "team_id", nullable = false)
    private TeamId teamId;

    private String writer;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Guestbook(TeamId teamId, String writer, String content) {
        if (teamId == null) {
            throw new GuestbookHandler(ErrorStatus.INVALID_TEAM_ID);
        }
        this.teamId = teamId;
        this.writer = writer;
        this.content = content;
    }
}
