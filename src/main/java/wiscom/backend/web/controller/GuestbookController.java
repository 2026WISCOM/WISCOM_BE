package wiscom.backend.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wiscom.backend.apiPayload.ApiResponse;
import wiscom.backend.service.GuestbookService.GuestbookService;
import wiscom.backend.web.dto.guestbook.GuestbookRequestDTO;
import wiscom.backend.web.dto.guestbook.GuestbookResponseDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guestbooks")
public class GuestbookController {
    private final GuestbookService guestbookService;

    @PostMapping
    public ApiResponse<GuestbookResponseDTO.GuestbookInfoDTO> createGuestbook(
            @Valid @RequestBody GuestbookRequestDTO.CreateRequestDTO request) {
        return ApiResponse.onSuccess(guestbookService.createGuestbook(request));
    }

    @GetMapping
    public ApiResponse<List<GuestbookResponseDTO.GuestbookInfoDTO>> getGuestbooks() {
        return ApiResponse.onSuccess(guestbookService.getGuestbooks());
    }
}
