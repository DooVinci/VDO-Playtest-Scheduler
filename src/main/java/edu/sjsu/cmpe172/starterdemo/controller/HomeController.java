package edu.sjsu.cmpe172.starterdemo.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = "text/html")
    public String home() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Playtest Scheduler</title>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'SF Pro Display', 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
                        background: #f5f5f7;
                        color: #1d1d1f;
                        -webkit-font-smoothing: antialiased;
                        min-height: 100vh;
                        display: flex; flex-direction: column;
                        align-items: center; justify-content: center;
                    }
                    .hero {
                        text-align: center; padding: 0 0 36px;
                    }
                    .hero h1 {
                        font-size: 48px; font-weight: 700; letter-spacing: -0.5px;
                        color: #1d1d1f; line-height: 1.1;
                    }
                    .hero .subtitle {
                        font-size: 19px; color: #86868b; margin-top: 12px;
                        font-weight: 400;
                    }
                    .card-grid {
                        display: flex; flex-wrap: wrap; justify-content: center;
                        gap: 20px; max-width: 680px;
                    }
                    .card {
                        background: #fff; border-radius: 18px; padding: 28px;
                        text-decoration: none; color: #1d1d1f;
                        transition: transform 0.2s, box-shadow 0.2s;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.04);
                        display: flex; flex-direction: column;
                        align-items: center; justify-content: center;
                        text-align: center;
                        width: 200px; height: 200px;
                    }
                    .card:hover {
                        transform: translateY(-4px);
                        box-shadow: 0 8px 24px rgba(0,0,0,0.08);
                    }
                    .card-icon { font-size: 32px; margin-bottom: 12px; }
                    .card-title { font-size: 18px; font-weight: 600; margin-bottom: 6px; }
                    .card-desc { font-size: 13px; color: #86868b; line-height: 1.4; }
                </style>
            </head>
            <body>
                <div class="hero">
                    <h1>Playtest Session Scheduler</h1>
                    <p class="subtitle">Game Studio QA Playtest Scheduling System</p>
                </div>
                <div class="card-grid">
                    <a href="/slots" class="card">
                        <div class="card-title">Available Slots</div>
                        <div class="card-desc">Browse open playtest sessions</div>
                    </a>
                    <a href="/appointments" class="card">
                        <div class="card-title">Appointments</div>
                        <div class="card-desc">View and manage bookings</div>
                    </a>
                    <a href="/book" class="card">
                        <div class="card-title">Book a Session</div>
                        <div class="card-desc">Reserve a playtest seat</div>
                    </a>
                    <a href="/manage" class="card">
                        <div class="card-title">Manage Sessions</div>
                        <div class="card-desc">Create and manage slots</div>
                    </a>
                    <a href="/health" class="card">
                        <div class="card-title">System Health</div>
                        <div class="card-desc">Check system status</div>
                    </a>
                </div>
            </body>
            </html>
            """;
    }
}