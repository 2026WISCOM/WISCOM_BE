package wiscom.backend.service.GuestbookService;

import wiscom.backend.web.dto.guestbook.GuestbookRequestDTO;
import wiscom.backend.web.dto.guestbook.GuestbookResponseDTO;

import java.util.List;

public interface GuestbookService {
    GuestbookResponseDTO.GuestbookInfoDTO createGuestbook(GuestbookRequestDTO.CreateRequestDTO request);
    List<GuestbookResponseDTO.GuestbookInfoDTO> getGuestbooks();
}
