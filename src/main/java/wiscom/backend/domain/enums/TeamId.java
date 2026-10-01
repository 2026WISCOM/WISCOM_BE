package wiscom.backend.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import wiscom.backend.apiPayload.code.status.ErrorStatus;
import wiscom.backend.apiPayload.exception.handler.GuestbookHandler;

public enum TeamId {
    DEADLOCK("데드락"),
    QUADCORE("Quadcore"),
    TEAM_2233("2233"),
    AJASS("아자쓰!"),
    ZERO_ONE_ZERO_ONE("공일공일"),
    POLYSTACK("PolyStack"),
    EXIT_0("exit(0)"),
    MOOD_E("MOOD:E"),
    TEAM_404("404"),
    BE1("BE1"),
    AXIS("Axis"),
    GUARDIANS("가디언즈"),
    EVERYONE("모두에게");

    private final String value;

    TeamId(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static TeamId fromValue(String value) {
        for (TeamId teamId : values()) {
            if (teamId.value.equals(value)) {
                return teamId;
            }
        }
        throw new GuestbookHandler(ErrorStatus.INVALID_TEAM_ID);
    }

    // Object prevents Jackson from coercing JSON numbers such as 2233 into a team name.
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TeamId fromJson(Object value) {
        if (value instanceof String teamName) {
            return fromValue(teamName);
        }
        throw new GuestbookHandler(ErrorStatus.INVALID_TEAM_ID);
    }
}
