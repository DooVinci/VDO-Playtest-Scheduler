package edu.sjsu.cmpe172.starterdemo.controller;

public class PageLayout {
    private PageLayout() {}

    public static String wrap(String title, String bodyContent) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>%s</title>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }

                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'SF Pro Display', 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
                        background: #f5f5f7;
                        color: #1d1d1f;
                        -webkit-font-smoothing: antialiased;
                        line-height: 1.5;
                    }

                    /* ---- Nav Bar ---- */
                    nav.topbar {
                        position: sticky; top: 0; z-index: 100;
                        background: rgba(251, 251, 253, 0.8);
                        backdrop-filter: saturate(180%%) blur(20px);
                        -webkit-backdrop-filter: saturate(180%%) blur(20px);
                        border-bottom: 1px solid rgba(0,0,0,0.08);
                        padding: 0 40px;
                        display: flex; flex-direction: column; align-items: center; justify-content: center;
                        padding-top: 10px; padding-bottom: 10px;
                        gap: 6px;
                    }
                    nav.topbar .brand {
                        font-size: 18px; font-weight: 600; color: #1d1d1f;
                        text-decoration: none; letter-spacing: -0.3px;
                    }
                    nav.topbar .nav-links { display: flex; gap: 28px; }
                    nav.topbar .nav-links a {
                        font-size: 13px; color: #424245; text-decoration: none;
                        font-weight: 400; transition: color 0.2s;
                    }
                    nav.topbar .nav-links a:hover { color: #0071e3; }

                    .container {
                        max-width: 980px; margin: 0 auto; padding: 48px 20px 80px;
                    }

                    .hero {
                        text-align: center; padding: 60px 0 40px;
                    }
                    .hero h1 {
                        font-size: 48px; font-weight: 700; letter-spacing: -0.5px;
                        color: #1d1d1f; line-height: 1.1;
                    }
                    .hero .subtitle {
                        font-size: 19px; color: #86868b; margin-top: 12px;
                        font-weight: 400;
                    }

                    /* ---- Page Title ---- */
                    .page-header {
                        padding: 40px 0 24px;
                    }
                    .page-header h1 {
                        font-size: 34px; font-weight: 700; letter-spacing: -0.3px;
                        color: #1d1d1f;
                    }
                    .page-header p {
                        font-size: 17px; color: #86868b; margin-top: 6px;
                    }

                    /* ---- Card Grid ---- */
                    .card-grid {
                        display: flex; flex-wrap: wrap; justify-content: center;
                        gap: 20px; margin-top: 20px;
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
                    .card-title { font-size: 20px; font-weight: 600; margin-bottom: 6px; }
                    .card-desc { font-size: 14px; color: #86868b; line-height: 1.4; }

                    /* ---- Table ---- */
                    .data-table-wrapper {
                        background: #fff; border-radius: 18px;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.04);
                        overflow: hidden; margin-top: 20px;
                    }
                    table.data-table {
                        width: 100%%; border-collapse: collapse;
                    }
                    table.data-table th {
                        text-align: left; padding: 14px 18px;
                        font-size: 12px; font-weight: 600; color: #86868b;
                        text-transform: uppercase; letter-spacing: 0.5px;
                        background: #fafafa; border-bottom: 1px solid #e8e8ed;
                    }
                    table.data-table td {
                        padding: 14px 18px; font-size: 14px;
                        border-bottom: 1px solid #f0f0f5; color: #1d1d1f;
                    }
                    table.data-table tr:last-child td { border-bottom: none; }
                    table.data-table tr:hover td { background: #fafafe; }

                    /* ---- Status ---- */
                    .badge {
                        display: inline-block; padding: 3px 10px; border-radius: 12px;
                        font-size: 12px; font-weight: 600; letter-spacing: 0.3px;
                    }
                    .badge-open { background: #e8f5e9; color: #2e7d32; }
                    .badge-booked { background: #e3f2fd; color: #1565c0; }
                    .badge-canceled { background: #fce4ec; color: #c62828; }

                    /* ---- Links ---- */
                    .action-link {
                        font-size: 13px; color: #0071e3; text-decoration: none;
                        font-weight: 500; cursor: pointer;
                        transition: color 0.15s;
                    }
                    .action-link:hover { color: #0077ed; text-decoration: underline; }
                    .action-sep { color: #d2d2d7; margin: 0 6px; }

                    /* ---- Form ---- */
                    .form-card {
                        background: #fff; border-radius: 18px; padding: 36px;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.04);
                        max-width: 480px; margin-top: 20px;
                    }
                    .form-card label {
                        display: block; font-size: 13px; font-weight: 600;
                        color: #86868b; text-transform: uppercase; letter-spacing: 0.5px;
                        margin-bottom: 6px; margin-top: 20px;
                    }
                    .form-card label:first-child { margin-top: 0; }
                    .form-card select,
                    .form-card input[type="text"] {
                        width: 100%%; padding: 10px 14px; font-size: 16px;
                        border: 1px solid #d2d2d7; border-radius: 10px;
                        background: #fafafa; color: #1d1d1f;
                        font-family: inherit; outline: none;
                        transition: border-color 0.2s;
                    }
                    .form-card select:focus,
                    .form-card input[type="text"]:focus {
                        border-color: #0071e3;
                    }

                    /* ---- Button ---- */
                    .btn-primary {
                        display: inline-block; margin-top: 28px;
                        padding: 12px 28px; font-size: 16px; font-weight: 500;
                        color: #fff; background: #0071e3; border: none;
                        border-radius: 12px; cursor: pointer;
                        font-family: inherit; letter-spacing: -0.2px;
                        transition: background 0.2s;
                    }
                    .btn-primary:hover { background: #0077ed; }

                    /* ---- Result Msg ---- */
                    .result-msg {
                        margin-top: 20px; padding: 16px 20px;
                        border-radius: 12px; font-size: 15px;
                        background: #f0f8ff; color: #1d1d1f;
                        border: 1px solid #d6e9f8;
                    }

                    /* ---- Back Link ---- */
                    .back-link {
                        display: inline-block; margin-top: 28px;
                        font-size: 14px; color: #0071e3; text-decoration: none;
                    }
                    .back-link:hover { text-decoration: underline; }

                    /* ---- Empty State ---- */
                    .empty-state {
                        text-align: center; padding: 60px 20px; color: #86868b;
                        font-size: 16px;
                    }
                </style>
            </head>
            <body>
                <nav class="topbar">
                    <a href="/" class="brand">Game QA Test Reservation Site</a>
                    <div class="nav-links">
                        <a href="/slots">Slots</a>
                        <a href="/appointments">Appointments</a>
                        <a href="/book">Book</a>
                        <a href="/manage">Manage</a>
                        <a href="/health">Health</a>
                    </div>
                </nav>
                <div class="container">
                    %s
                </div>
            </body>
            </html>
            """.formatted(title, bodyContent);
    }
}