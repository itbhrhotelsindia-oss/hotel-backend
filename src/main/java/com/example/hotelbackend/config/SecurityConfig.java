package com.example.hotelbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // ── CORS preflight ────────────────────────────────────────
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ── Auth (login) ──────────────────────────────────────────
                .requestMatchers("/api/auth/**").permitAll()

                // ── Public booking flow ───────────────────────────────────
                .requestMatchers("/api/public/**").permitAll()

                // ── Health ────────────────────────────────────────────────
                .requestMatchers("/api/health").permitAll()

                // ── Public CMS reads ──────────────────────────────────────
                .requestMatchers(HttpMethod.GET, "/api/home").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/events/page").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/weddings/page").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/legal/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/offers/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/blog-home/**").permitAll()

                // ── Public hotel / city reads ─────────────────────────────
                // Specific paths MUST come before the generic {id} wildcard
                .requestMatchers(HttpMethod.GET, "/api/cities/all").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/cities/").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/cities/hotels/lookup").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/cities/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/room-types").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/our-hotels").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/our-hotels/**").permitAll()

                // Public: single hotel detail page (GET by hotelId)
                // Must come before the SUPER_ADMIN rule for GET /api/hotel-details
                .requestMatchers(HttpMethod.GET, "/api/hotel-details/*").permitAll()

                // ── Public enquiry submissions ────────────────────────────
                .requestMatchers(HttpMethod.POST, "/api/events/enquiry").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/weddings/enquiry").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/partner-with-us").permitAll()

                // ── OWNER + SUPER_ADMIN: room types & inventory ───────────
                // Must come BEFORE the general /api/admin/** rule
                .requestMatchers("/api/admin/room-types/**").hasAnyAuthority("OWNER", "SUPER_ADMIN")
                .requestMatchers("/api/admin/inventory/**").hasAnyAuthority("OWNER", "SUPER_ADMIN")

                // ── SUPER_ADMIN only: everything else in admin ────────────
                .requestMatchers("/api/admin/**").hasAuthority("SUPER_ADMIN")

                // Cities writes
                .requestMatchers(HttpMethod.POST,   "/api/cities/**").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/cities/**").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/cities/**").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.PATCH,  "/api/cities/**").hasAuthority("SUPER_ADMIN")

                // Hotel details: list-all (admin) + all writes
                .requestMatchers(HttpMethod.GET,    "/api/hotel-details").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.POST,   "/api/hotel-details/**").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/hotel-details/**").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/hotel-details/**").hasAuthority("SUPER_ADMIN")

                // Home / events / wedding page writes
                .requestMatchers(HttpMethod.PUT, "/api/home").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/home/all").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/events/page").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/weddings/page").hasAuthority("SUPER_ADMIN")

                // Enquiry management (view + delete)
                .requestMatchers(HttpMethod.GET,    "/api/events/enquiry/**").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/events/enquiry/**").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.GET,    "/api/weddings/enquiry/**").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/weddings/enquiry/**").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.GET,    "/api/partner-with-us/**").hasAuthority("SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/partner-with-us/**").hasAuthority("SUPER_ADMIN")

                // ── OWNER or SUPER_ADMIN ──────────────────────────────────
                .requestMatchers("/api/owner/**").hasAnyAuthority("OWNER", "SUPER_ADMIN")

                // ── Fallback: require authentication ─────────────────────
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
