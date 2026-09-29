package wiscom.backend.service.GuestbookService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiscom.backend.converter.GuestbookConverter;
import wiscom.backend.domain.Guestbook;
import wiscom.backend.repository.GuestbookRepository;
import wiscom.backend.web.dto.guestbook.GuestbookRequestDTO;
import wiscom.backend.web.dto.guestbook.GuestbookResponseDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuestbookServiceImpl implements GuestbookService {
    private final GuestbookRepository guestbookRepository;

    @Override
    @Transactional
    public GuestbookResponseDTO.GuestbookInfoDTO createGuestbook(GuestbookRequestDTO.CreateRequestDTO request) {
        Guestbook guestbook = guestbookRepository.save(GuestbookConverter.toGuestbook(request));
        return GuestbookConverter.toGuestbookInfoDTO(guestbook);
    }

    @Override
    public List<GuestbookResponseDTO.GuestbookInfoDTO> getGuestbooks() {
        return guestbookRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(GuestbookConverter::toGuestbookInfoDTO)
                .toList();
    }
}
