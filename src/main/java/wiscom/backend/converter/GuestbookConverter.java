package wiscom.backend.converter;

import wiscom.backend.domain.Guestbook;
import wiscom.backend.web.dto.guestbook.GuestbookRequestDTO;
import wiscom.backend.web.dto.guestbook.GuestbookResponseDTO;

public class GuestbookConverter {
    public static Guestbook toGuestbook(GuestbookRequestDTO.CreateRequestDTO request) {
        return new Guestbook(request.getTeamId(), request.getWriter(), request.getContent());
    }

    public static GuestbookResponseDTO.GuestbookInfoDTO toGuestbookInfoDTO(Guestbook guestbook) {
        return GuestbookResponseDTO.GuestbookInfoDTO.builder()
                .id(guestbook.getId())
                .teamId(guestbook.getTeamId())
                .writer(guestbook.getWriter())
                .content(guestbook.getContent())
                .createdAt(guestbook.getCreatedAt())
                .build();
    }
}
