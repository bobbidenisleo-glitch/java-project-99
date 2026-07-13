package hexlet.code.app.service;

import hexlet.code.app.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtService jwtService;

    private String testSecret;
    private Long testExpiration;
    private String testEmail;

    @BeforeEach
    void setUp() {
        testSecret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        testExpiration = 86400000L;
        testEmail = "test@example.com";

        when(jwtConfig.getSecret()).thenReturn(testSecret);
        when(jwtConfig.getExpiration()).thenReturn(testExpiration);
        when(userDetails.getUsername()).thenReturn(testEmail);
    }

    @Test
    void testExtractUsername_ShouldReturnEmail_WhenTokenIsValid() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo(testEmail);
    }

    @Test
    void testExtractExpiration_ShouldReturnDate_WhenTokenIsValid() {
        String token = jwtService.generateToken(userDetails);
        Date expiration = jwtService.extractExpiration(token);

        assertThat(expiration).isNotNull();
        assertThat(expiration).isAfter(new Date());
    }

    @Test
    void testGenerateToken_ShouldReturnToken_WhenUserDetailsProvided() {
        String token = jwtService.generateToken(userDetails);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void testValidateToken_ShouldReturnTrue_WhenTokenIsValid() {
        String token = jwtService.generateToken(userDetails);

        Boolean isValid = jwtService.validateToken(token, userDetails);

        assertThat(isValid).isTrue();
    }

    @Test
    void testValidateToken_ShouldReturnFalse_WhenUsernameDoesNotMatch() {
        String token = jwtService.generateToken(userDetails);

        UserDetails differentUser = org.springframework.security.core.userdetails.User
                .withUsername("different@example.com")
                .password("password")
                .roles("USER")
                .build();

        Boolean isValid = jwtService.validateToken(token, differentUser);

        assertThat(isValid).isFalse();
    }

    @Test
    void testValidateToken_ShouldThrowException_WhenTokenIsExpired() throws InterruptedException {
        when(jwtConfig.getExpiration()).thenReturn(50L);

        String token = jwtService.generateToken(userDetails);

        Thread.sleep(100);

        try {
            jwtService.validateToken(token, userDetails);
            fail("Expected ExpiredJwtException");
        } catch (ExpiredJwtException e) {
            assertThat(e).isInstanceOf(ExpiredJwtException.class);
        }
    }

    @Test
    void testExtractClaim_ShouldReturnClaim_WhenTokenIsValid() {
        String token = jwtService.generateToken(userDetails);

        String subject = jwtService.extractClaim(token, Claims::getSubject);

        assertThat(subject).isEqualTo(testEmail);
    }

    @Test
    void testGenerateToken_ShouldCreateTokenWithCorrectSubject() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo(testEmail);
    }

        @Test
    void testGenerateToken_ShouldCreateTokenWithCorrectIssuedAt() {
        // Просто проверяем, что issuedAt не null и не в будущем
        String token = jwtService.generateToken(userDetails);
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);
        Date now = new Date();

        // issuedAt должен быть не null и не позже текущего времени
        assertThat(issuedAt).isNotNull();
        assertThat(issuedAt).isBeforeOrEqualTo(now);
    }

    @Test
    void testGenerateToken_ShouldCreateTokenWithCorrectExpiration() {
        String token = jwtService.generateToken(userDetails);
        Date expiration = jwtService.extractExpiration(token);

        Date expectedExpiration = new Date(System.currentTimeMillis() + testExpiration);
        long diff = Math.abs(expiration.getTime() - expectedExpiration.getTime());

        assertThat(diff).isLessThan(1000);
    }

    @Test
    void testGenerateToken_WithNullClaims() {
        String token = jwtService.generateToken(userDetails);

        assertThat(token).isNotNull();
    }
}
