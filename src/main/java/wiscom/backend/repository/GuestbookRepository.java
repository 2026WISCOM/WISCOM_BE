package wiscom.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wiscom.backend.domain.Guestbook;

import java.util.List;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {
    List<Guestbook> findAllByOrderByCreatedAtDesc();
}
