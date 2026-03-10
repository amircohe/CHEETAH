<!DOCTYPE html>
<html lang="he" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>פורטל שיבוץ משמרות | Cheetah Jobs</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/PapaParse/5.3.0/papaparse.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@300;400;500;700;900&display=swap" rel="stylesheet">
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    fontFamily: { sans: ['Heebo', 'sans-serif'] },
                    colors: { brand: { 500: '#3b82f6', 600: '#2563eb', 900: '#1e3a8a' } },
                    boxShadow: { 'soft': '0 20px 40px -15px rgba(0,0,0,0.05)', 'glow': '0 0 20px rgba(59, 130, 246, 0.5)' }
                }
            }
        }
    </script>
    <style>
        :root { --indigo: #5a5af5; --violet: #7c3aed; --surface: #f4f6fb; --border: #e2e8f4; --text: #0f172a; --muted: #64748b; }
        #modal, #permanentModal { overflow-x: hidden; }
        #modal *, #permanentModal * { max-width: 100%; }
        body { background-color: #f8fafc; background-image: radial-gradient(at 0% 0%, rgba(59,130,246,0.08) 0px, transparent 50%), radial-gradient(at 100% 100%, rgba(236,72,153,0.05) 0px, transparent 50%); background-attachment: fixed; }
        .glass { background: rgba(255,255,255,0.85); backdrop-filter: blur(16px); -webkit-backdrop-filter: blur(16px); border-bottom: 1px solid rgba(255,255,255,0.6); }

        .ticket-card { background: white; border-radius: 24px; box-shadow: 0 10px 30px -10px rgba(0,0,0,0.03); transition: all 0.3s cubic-bezier(0.34,1.56,0.64,1); border: 1px solid #f1f5f9; overflow: hidden; position: relative; display: flex; flex-direction: column; }
        .ticket-card:hover { transform: translateY(-8px) scale(1.01); box-shadow: 0 25px 50px -12px rgba(59,130,246,0.15); border-color: #bfdbfe; }

        .re-card { background: white; border-radius: 24px; overflow: hidden; position: relative; box-shadow: 0 2px 20px rgba(15,23,42,0.06); border: 1px solid #f0f2f8; transition: transform 0.34s cubic-bezier(0.34,1.4,0.64,1), box-shadow 0.34s ease; display: flex; flex-direction: column; }
        .re-card:hover { transform: translateY(-8px); box-shadow: 0 20px 50px -12px rgba(15,23,42,0.16); }
        .re-card-hero { position: relative; height: 190px; flex-shrink: 0; border-radius: 18px; margin: 10px 10px 0; overflow: hidden; }
        .re-card-hero img { width: 100%; height: 100%; object-fit: cover; display: block; transition: transform 0.6s ease; }
        .re-card:hover .re-card-hero img { transform: scale(1.05); }
        .re-card-hero-overlay { position: absolute; inset: 0; background: linear-gradient(to bottom, transparent 50%, rgba(10,12,30,0.5) 100%); border-radius: 16px; }
        .re-card-type-badge { position: absolute; bottom: 12px; right: 12px; background: rgba(15,20,40,0.85); backdrop-filter: blur(10px); color: white; font-size: 11px; font-weight: 900; padding: 4px 12px; border-radius: 99px; border: 1px solid rgba(255,255,255,0.15); max-width: calc(100% - 24px); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
        .re-card-fav { position: absolute; top: 10px; left: 10px; width: 32px; height: 32px; background: rgba(255,255,255,0.93); border-radius: 50%; display: flex; align-items: center; justify-content: center; cursor: pointer; border: none; box-shadow: 0 2px 8px rgba(0,0,0,0.1); transition: all 0.2s; }
        .re-card-fav:hover { transform: scale(1.12); background: white; }
        .re-card-fav svg { width: 15px; height: 15px; color: #94a3b8; transition: all 0.2s; }
        .re-card-fav.active svg { color: #e11d48; fill: #e11d48; }
        .re-card-body { padding: 14px 16px 0; overflow: hidden; }
        .re-card-address { font-size: 11px; font-weight: 700; color: #94a3b8; margin-bottom: 2px; display: flex; align-items: center; gap: 3px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
        .re-card-address svg { width: 10px; height: 10px; flex-shrink: 0; }
        .re-card-title { font-size: 12.5px; font-weight: 800; color: #64748b; margin-bottom: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
        .re-card-price { font-size: 22px; font-weight: 900; color: #0f172a; line-height: 1.15; letter-spacing: -0.3px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
        .re-card-chips { display: flex; gap: 7px; margin-top: 12px; overflow-x: auto; -webkit-overflow-scrolling: touch; scrollbar-width: none; padding-bottom: 2px; }
        .re-card-chips::-webkit-scrollbar { display: none; }
        .re-card-chip { display: flex; align-items: center; gap: 5px; background: #f4f6fb; border: 1.5px solid #e8eaf2; border-radius: 10px; padding: 6px 11px; font-size: 11.5px; font-weight: 800; color: #334155; flex-shrink: 0; white-space: nowrap; max-width: 160px; overflow: hidden; text-overflow: ellipsis; }
        .re-card-chip svg { width: 13px; height: 13px; color: #5a5af5; flex-shrink: 0; }
        .re-card-footer { margin-top: auto; padding: 14px 16px 16px; }
        .re-card-footer-row { display: flex; align-items: center; gap: 9px; }
        .re-book-btn-wrap { flex: 1; min-width: 0; }
        .re-book-btn { width: 100%; padding: 13px 16px; background: #d4f04a; color: #1a2000; font-weight: 900; font-size: 14px; border-radius: 13px; border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 7px; transition: all 0.22s; box-shadow: 0 3px 14px -3px rgba(180,220,30,0.45); white-space: nowrap; overflow: hidden; }
        .re-book-btn:hover { background: #c8e83a; transform: translateY(-2px); box-shadow: 0 8px 22px -4px rgba(160,200,20,0.5); }
        .re-book-btn svg { width: 15px; height: 15px; flex-shrink: 0; }
        .re-details-btn { display: flex; align-items: center; justify-content: center; width: 42px; height: 42px; flex-shrink: 0; background: #f4f6fb; border: 1.5px solid #e8eaf2; border-radius: 11px; cursor: pointer; color: #5a5af5; transition: all 0.18s; }
        .re-details-btn:hover { background: #eef0ff; border-color: #c4c4f8; }
        .re-details-btn svg { width: 15px; height: 15px; }

        #announcementStrip { display: none; position: relative; overflow: hidden; background: #0f172a; transition: background 0.9s ease; }
        #announcementStrip.visible { display: block; }
        #announcementStrip.single-item { height: 52px; }
        #announcementStrip.multi-item  { height: 52px; }
        #announcementStrip.stype-info    { background: linear-gradient(135deg, #0f172a 0%, #1e3a8a 60%, #1d4ed8 100%); }
        #announcementStrip.stype-urgent  { background: linear-gradient(135deg, #450a0a 0%, #7f1d1d 60%, #dc2626 100%); }
        #announcementStrip.stype-warning { background: linear-gradient(135deg, #1c1006 0%, #78350f 60%, #d97706 100%); }
        #announcementStrip.stype-success { background: linear-gradient(135deg, #052e16 0%, #14532d 60%, #16a34a 100%); }
        #announcementStrip.stype-new     { background: linear-gradient(135deg, #2e1065 0%, #4c1d95 60%, #7c3aed 100%); }
        #announcementStrip.stype-security { background: linear-gradient(135deg, #0c1b0c 0%, #14301a 60%, #15803d 100%); }
        .strip-accent-line { position: absolute; bottom: 0; left: 0; right: 0; height: 2px; z-index: 5; }
        #announcementStrip.stype-info    .strip-accent-line { background: linear-gradient(90deg, transparent, #60a5fa, transparent); }
        #announcementStrip.stype-urgent  .strip-accent-line { background: linear-gradient(90deg, transparent, #f87171, transparent); }
        #announcementStrip.stype-warning .strip-accent-line { background: linear-gradient(90deg, transparent, #fbbf24, transparent); }
        #announcementStrip.stype-success .strip-accent-line { background: linear-gradient(90deg, transparent, #4ade80, transparent); }
        #announcementStrip.stype-new     .strip-accent-line { background: linear-gradient(90deg, transparent, #a78bfa, transparent); }
        #announcementStrip.stype-security .strip-accent-line { background: linear-gradient(90deg, transparent, #4ade80, transparent); }
        .strip-single { display: flex; align-items: center; height: 52px; position: relative; z-index: 2; padding: 0 16px; gap: 12px; }
        .strip-single-icon { width: 28px; height: 28px; border-radius: 8px; background: rgba(255,255,255,0.12); border: 1px solid rgba(255,255,255,0.2); display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
        .strip-single-icon svg { width: 14px; height: 14px; color: white; }
        .strip-single-label { font-size: 10.5px; font-weight: 900; letter-spacing: 0.8px; text-transform: uppercase; color: rgba(255,255,255,0.6); white-space: nowrap; flex-shrink: 0; }
        .strip-single-sep { width: 1px; height: 18px; background: rgba(255,255,255,0.2); flex-shrink: 0; }
        .strip-single-text { font-size: 13.5px; font-weight: 600; color: rgba(255,255,255,0.95); flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
        .strip-single-date { font-size: 11px; font-weight: 700; color: rgba(255,255,255,0.45); white-space: nowrap; flex-shrink: 0; }
        .strip-single-cta { display: flex; align-items: center; gap: 5px; padding: 5px 12px; border-radius: 8px; background: rgba(255,255,255,0.12); border: 1px solid rgba(255,255,255,0.2); cursor: pointer; transition: background 0.2s; flex-shrink: 0; font-size: 11px; font-weight: 800; color: rgba(255,255,255,0.85); white-space: nowrap; }
        .strip-single-cta:hover { background: rgba(255,255,255,0.2); color: white; }
        .strip-single-cta svg { width: 11px; height: 11px; }
        .strip-inner { position: relative; z-index: 2; display: flex; align-items: center; height: 52px; }
        .strip-label-pill { display: flex; align-items: center; gap: 7px; padding: 0 14px 0 10px; height: 100%; flex-shrink: 0; border-left: 1px solid rgba(255,255,255,0.12); }
        .strip-type-dot { width: 7px; height: 7px; border-radius: 50%; flex-shrink: 0; animation: dotPulse 2.4s ease-in-out infinite; }
        @keyframes dotPulse { 0%, 100% { opacity: 1; transform: scale(1); } 50% { opacity: 0.5; transform: scale(0.75); } }
        #announcementStrip.stype-info     .strip-type-dot { background: #93c5fd; box-shadow: 0 0 5px #93c5fd; }
        #announcementStrip.stype-urgent   .strip-type-dot { background: #fca5a5; box-shadow: 0 0 5px #fca5a5; }
        #announcementStrip.stype-warning  .strip-type-dot { background: #fcd34d; box-shadow: 0 0 5px #fcd34d; }
        #announcementStrip.stype-success  .strip-type-dot { background: #86efac; box-shadow: 0 0 5px #86efac; }
        #announcementStrip.stype-new      .strip-type-dot { background: #c4b5fd; box-shadow: 0 0 5px #c4b5fd; }
        #announcementStrip.stype-security .strip-type-dot { background: #86efac; box-shadow: 0 0 5px #86efac; }
        .strip-type-text { font-size: 10px; font-weight: 900; letter-spacing: 1px; text-transform: uppercase; white-space: nowrap; color: rgba(255,255,255,0.85); }
        .strip-count-badge { display: inline-flex; align-items: center; justify-content: center; min-width: 17px; height: 17px; border-radius: 99px; background: rgba(255,255,255,0.16); border: 1px solid rgba(255,255,255,0.22); font-size: 10px; font-weight: 900; color: white; padding: 0 4px; flex-shrink: 0; }
        .strip-fade-wrap { flex: 1; position: relative; height: 100%; overflow: hidden; display: flex; align-items: center; }
        .strip-fade-item { position: absolute; inset: 0; display: flex; align-items: center; padding: 0 16px; gap: 10px; opacity: 0; transition: opacity 0.6s ease; pointer-events: none; }
        .strip-fade-item.active { opacity: 1; pointer-events: auto; }
        .strip-fade-cat { display: inline-flex; align-items: center; gap: 4px; padding: 2px 8px; border-radius: 99px; font-size: 9.5px; font-weight: 900; letter-spacing: 0.5px; text-transform: uppercase; background: rgba(255,255,255,0.13); border: 1px solid rgba(255,255,255,0.2); color: rgba(255,255,255,0.88); white-space: nowrap; flex-shrink: 0; }
        .strip-fade-cat svg { width: 9px; height: 9px; }
        .strip-fade-text { font-size: 13.5px; font-weight: 600; color: rgba(255,255,255,0.94); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; flex: 1; }
        .strip-fade-date { font-size: 11px; font-weight: 700; color: rgba(255,255,255,0.4); white-space: nowrap; flex-shrink: 0; }
        .strip-dots { display: flex; align-items: center; gap: 4px; padding: 0 10px; flex-shrink: 0; }
        .strip-dot { width: 5px; height: 5px; border-radius: 50%; background: rgba(255,255,255,0.3); transition: all 0.3s; cursor: pointer; border: none; padding: 0; }
        .strip-dot.active { background: rgba(255,255,255,0.9); transform: scale(1.3); }
        .strip-cta { flex-shrink: 0; height: 100%; display: flex; align-items: center; padding: 0 14px; border-right: 1px solid rgba(255,255,255,0.1); gap: 5px; cursor: pointer; transition: background 0.2s; background: rgba(255,255,255,0.07); }
        .strip-cta:hover { background: rgba(255,255,255,0.13); }
        .strip-cta-text { font-size: 11px; font-weight: 800; color: rgba(255,255,255,0.75); white-space: nowrap; }
        .strip-cta:hover .strip-cta-text { color: white; }
        .strip-cta svg { width: 12px; height: 12px; color: rgba(255,255,255,0.55); }
        .strip-cta:hover svg { color: white; }

        #announcementBtn { position: fixed; bottom: 24px; left: 24px; z-index: 50; width: auto; height: 46px; border-radius: 23px; background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%); border: none; cursor: pointer; display: none; align-items: center; justify-content: center; gap: 8px; padding: 0 18px 0 14px; box-shadow: 0 8px 24px rgba(15,23,42,0.3), 0 2px 8px rgba(15,23,42,0.15); transition: transform 0.25s cubic-bezier(0.34,1.56,0.64,1), box-shadow 0.25s, background 0.25s; }
        #announcementBtn.visible { display: flex; }
        #announcementBtn:hover { transform: translateY(-3px); box-shadow: 0 14px 36px rgba(15,23,42,0.35), 0 0 0 6px rgba(99,102,241,0.12); background: linear-gradient(135deg, #334155 0%, #1e293b 100%); }
        #announcementBtn .bell-icon { width: 18px; height: 18px; color: white; flex-shrink:0; }
        #announcementBtn .bell-label { font-size: 12px; font-weight: 800; color: rgba(255,255,255,0.9); white-space: nowrap; letter-spacing: 0.2px; }
        #announcementBtn.has-new { animation: bellBounce 3s ease-in-out infinite; }
        @keyframes bellBounce { 0%,90%,100% { transform: translateY(0); } 93% { transform: translateY(-4px); } 96% { transform: translateY(-2px); } }
        #announcementBadge { display: none; align-items: center; justify-content: center; min-width: 18px; height: 18px; background: linear-gradient(135deg, #ef4444, #dc2626); color: white; font-size: 10px; font-weight: 900; border-radius: 99px; padding: 0 4px; box-shadow: 0 2px 6px rgba(220,38,38,0.5); border: 1.5px solid #0f172a; flex-shrink: 0; }
        #announcementBadge.visible { display: flex; }
        @media (max-width: 639px) {
            #announcementBtn { bottom: 18px; left: 18px; width: 48px; height: 48px; border-radius: 50%; padding: 0; gap: 0; }
            #announcementBtn .bell-label { display: none; }
            #announcementBtn .bell-icon { width: 20px; height: 20px; }
            #announcementBadge { position: absolute; top: -4px; left: -4px; border: 2px solid white; }
            .strip-single { padding: 8px 10px; gap: 8px; }
            .strip-single-date { display: none; }
        }

        #announcementPanel { position: fixed; bottom: 0; left: 0; width: 100%; max-width: 420px; height: 85dvh; background: #ffffff; border-radius: 24px 24px 0 0; box-shadow: 0 -16px 60px rgba(15,23,42,0.22), 0 -1px 0 rgba(99,102,241,0.2); z-index: 200; display: flex; flex-direction: column; overflow: hidden; transform: translateY(110%); transition: transform 0.42s cubic-bezier(0.34,1.25,0.64,1); }
        @media (min-width: 640px) { #announcementPanel { bottom: 24px; left: 24px; border-radius: 22px; height: 70dvh; max-height: 600px; } }
        #announcementPanel.open { transform: translateY(0); }
        #panelBackdrop { position: fixed; inset: 0; background: rgba(8,10,24,0.5); backdrop-filter: blur(6px); z-index: 199; opacity: 0; pointer-events: none; transition: opacity 0.3s; }
        #panelBackdrop.visible { opacity: 1; pointer-events: auto; }
        .panel-handle { width: 36px; height: 4px; background: #e2e8f0; border-radius: 99px; margin: 10px auto 0; flex-shrink: 0; }
        @media (min-width: 640px) { .panel-handle { display: none; } }
        .panel-header { display: flex; align-items: center; justify-content: space-between; padding: 14px 18px 12px; border-bottom: 1px solid #f1f5f9; flex-shrink: 0; }
        .panel-header-left { display: flex; align-items: center; gap: 10px; }
        .panel-header-icon { width: 36px; height: 36px; border-radius: 12px; background: linear-gradient(135deg, #eef2ff, #e0e7ff); display: flex; align-items: center; justify-content: center; }
        .panel-header-icon svg { width: 18px; height: 18px; color: #5a5af5; }
        .panel-header-title { font-size: 16px; font-weight: 900; color: #0f172a; }
        .panel-header-sub { font-size: 11px; font-weight: 700; color: #94a3b8; margin-top: 1px; }
        .panel-close { width: 32px; height: 32px; border-radius: 50%; background: #f1f5f9; border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; color: #64748b; transition: all 0.18s; }
        .panel-close:hover { background: #fee2e2; color: #e11d48; transform: rotate(90deg); }
        .panel-close svg { width: 14px; height: 14px; }
        .panel-tabs-wrapper { position: relative; flex-shrink: 0; }
        .tabs-arrow { display: none; position: absolute; top: 50%; transform: translateY(-50%); z-index: 10; width: 26px; height: 26px; border-radius: 50%; background: white; border: 1.5px solid #e2e8f0; box-shadow: 0 2px 8px rgba(0,0,0,0.1); cursor: pointer; align-items: center; justify-content: center; color: #475569; transition: all 0.18s; padding: 0; }
        .tabs-arrow:hover { background: #f1f5f9; color: #0f172a; border-color: #cbd5e0; }
        .tabs-arrow svg { width: 12px; height: 12px; }
        .tabs-arrow.arrow-right { right: 6px; }
        .tabs-arrow.arrow-left  { left: 6px; }
        .panel-tabs-wrapper::before, .panel-tabs-wrapper::after { content: ''; position: absolute; top: 0; bottom: 0; width: 36px; pointer-events: none; z-index: 5; opacity: 0; transition: opacity 0.2s; }
        .panel-tabs-wrapper::before { right: 0; background: linear-gradient(to left, white 60%, transparent); }
        .panel-tabs-wrapper::after  { left: 0;  background: linear-gradient(to right, white 60%, transparent); }
        .panel-tabs-wrapper.can-scroll-right::before { opacity: 1; }
        .panel-tabs-wrapper.can-scroll-left::after   { opacity: 1; }
        @media (min-width: 640px) { .tabs-arrow { display: flex; } .panel-tabs { padding-left: 34px; padding-right: 34px; } }
        .panel-tabs { display: flex; gap: 5px; padding: 10px 14px; border-bottom: 1px solid #f1f5f9; flex-shrink: 0; overflow-x: auto; -webkit-overflow-scrolling: touch; scroll-behavior: smooth; -ms-overflow-style: none; }
        .panel-tabs::-webkit-scrollbar { display: none; }
        .panel-tabs { scrollbar-width: none; }
        .panel-tab { display: inline-flex; align-items: center; gap: 4px; padding: 5px 11px; border-radius: 99px; font-size: 12px; font-weight: 800; cursor: pointer; border: 1.5px solid #e2e8f0; background: #f8fafc; color: #64748b; transition: all 0.18s; white-space: nowrap; flex-shrink: 0; }
        .panel-tab:hover { border-color: #c7d2e8; color: #334155; }
        .panel-tab.active { background: #0f172a; color: white; border-color: #0f172a; }
        .panel-tab svg { width: 11px; height: 11px; }
        .panel-scroll { overflow-y: auto; flex: 1; min-height: 0; padding: 12px 14px 16px; display: flex; flex-direction: column; gap: 10px; -webkit-overflow-scrolling: touch; }
        .panel-scroll::-webkit-scrollbar { width: 4px; }
        .panel-scroll::-webkit-scrollbar-thumb { background: #e2e8f0; border-radius: 20px; }
        .ann-card { border-radius: 16px; border: 1.5px solid #f1f5f9; overflow: visible; background: white; transition: border-color 0.2s, box-shadow 0.2s; }
        .ann-card:hover { border-color: #e0e7ff; box-shadow: 0 4px 16px rgba(99,102,241,0.08); }
        .ann-card-top { display: flex; align-items: flex-start; gap: 11px; padding: 13px 14px 10px; }
        .ann-type-icon { width: 34px; height: 34px; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
        .ann-type-icon svg { width: 16px; height: 16px; }
        .atype-info    { background: #eff6ff; color: #2563eb; }
        .atype-success { background: #f0fdf4; color: #16a34a; }
        .atype-warning { background: #fffbeb; color: #d97706; }
        .atype-urgent  { background: #fff1f2; color: #e11d48; }
        .atype-new     { background: #f5f3ff; color: #7c3aed; }
        .atype-security { background: #f0fdf4; color: #166534; }
        .ann-body { flex: 1; min-width: 0; }
        .ann-type-label { font-size: 10px; font-weight: 900; letter-spacing: 0.5px; text-transform: uppercase; margin-bottom: 3px; }
        .ann-text { font-size: 13.5px; font-weight: 600; color: #1e293b; line-height: 1.65; white-space: pre-line; word-break: break-word; overflow-wrap: break-word; }
        .ann-card-bottom { display: flex; align-items: center; justify-content: space-between; padding: 7px 14px 10px; background: #fafbff; border-top: 1px solid #f1f5f9; border-radius: 0 0 14px 14px; }
        .ann-date-display { font-size: 10px; font-weight: 800; color: #94a3b8; display: flex; align-items: center; gap: 4px; }
        .ann-date-display svg { width: 10px; height: 10px; }
        .ann-type-badge { font-size: 10px; font-weight: 900; padding: 2px 8px; border-radius: 99px; letter-spacing: 0.3px; }
        .panel-empty { display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 40px 20px; text-align: center; gap: 10px; }
        .panel-empty-icon { width: 56px; height: 56px; border-radius: 18px; background: #f1f5f9; display: flex; align-items: center; justify-content: center; margin-bottom: 4px; }
        .panel-empty-icon svg { width: 28px; height: 28px; color: #94a3b8; }
        .panel-empty h4 { font-size: 15px; font-weight: 900; color: #334155; }
        .panel-empty p  { font-size: 13px; color: #94a3b8; font-weight: 500; }

        /* ══ JOB DETAILS MODAL ══ */
        .jdm-backdrop { display: none; position: fixed; inset: 0; background: rgba(8,10,24,0.6); backdrop-filter: blur(10px); z-index: 60; align-items: flex-end; justify-content: center; }
        .jdm-backdrop.active { display: flex; }
        .jdm-container { background: #eef1f7; width: 100%; max-width: 100%; border-radius: 28px 28px 0 0; max-height: 95dvh; display: flex; flex-direction: column; overflow: hidden; box-shadow: 0 -20px 60px rgba(0,0,0,0.22); animation: jdmSlideUp 0.4s cubic-bezier(0.34,1.2,0.64,1) forwards; }
        @keyframes jdmSlideUp { from { transform: translateY(80px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
        @media (min-width: 600px) { .jdm-backdrop { align-items: center; padding: 24px; } .jdm-container { max-width: 440px; border-radius: 28px; max-height: 92dvh; animation: jdmFadeScale 0.36s cubic-bezier(0.34,1.2,0.64,1) forwards; } }
        @media (min-width: 1024px) { .jdm-backdrop { padding: 40px; } .jdm-container { max-width: 460px; } }
        @keyframes jdmFadeScale { from { transform: scale(0.93) translateY(18px); opacity: 0; } to { transform: scale(1) translateY(0); opacity: 1; } }
        .jdm-backdrop.fullscreen { padding: 0; align-items: stretch; justify-content: stretch; }
        .jdm-backdrop.fullscreen .jdm-container { max-width: 100%; width: 100%; height: 100dvh; max-height: 100dvh; border-radius: 0; flex-direction: row; }
        .jdm-left-panel { display: none; }
        .jdm-backdrop.fullscreen .jdm-left-panel { display: flex; flex-direction: column; width: 38%; min-width: 300px; max-width: 480px; flex-shrink: 0; background: #141820; overflow: hidden; }
        .jdm-left-panel-hero { position: relative; width: 100%; height: 260px; flex-shrink: 0; overflow: hidden; }
        .jdm-left-panel-hero img { width: 100%; height: 100%; object-fit: cover; object-position: center center; display: block; }
        .jdm-left-panel-overlay { position: absolute; inset: 0; background: linear-gradient(to bottom, transparent 40%, rgba(20,24,32,0.8) 100%); }
        .jdm-left-panel-info { flex: 1; padding: 24px 26px 28px; overflow-y: auto; background: #141820; }
        .jdm-left-panel-type { display: inline-flex; align-items: center; background: rgba(255,255,255,0.12); border: 1px solid rgba(255,255,255,0.2); color: rgba(255,255,255,0.85); font-size: 11px; font-weight: 800; padding: 3px 12px; border-radius: 99px; margin-bottom: 12px; letter-spacing: 0.3px; }
        .jdm-left-panel-title { font-size: 22px; font-weight: 900; color: white; line-height: 1.2; margin-bottom: 4px; }
        .jdm-left-panel-company { font-size: 13px; font-weight: 600; color: rgba(255,255,255,0.5); margin-bottom: 16px; }
        .jdm-left-panel-salary { font-size: 30px; font-weight: 900; color: #d4f04a; letter-spacing: -0.5px; line-height: 1; }
        .jdm-left-panel-location { display: flex; align-items: center; gap: 5px; font-size: 12px; font-weight: 600; color: rgba(255,255,255,0.4); margin-top: 8px; }
        .jdm-left-panel-location svg { width: 11px; height: 11px; flex-shrink: 0; }
        .jdm-left-panel-chips { display: flex; flex-direction: column; gap: 6px; margin-top: 14px; }
        .jdm-left-chip { display: flex; align-items: center; gap: 8px; background: rgba(255,255,255,0.08); border: 1px solid rgba(255,255,255,0.12); border-radius: 10px; padding: 8px 12px; font-size: 12px; font-weight: 700; color: rgba(255,255,255,0.8); }
        .jdm-left-chip svg { width: 13px; height: 13px; color: #818cf8; flex-shrink: 0; }
        .jdm-right-panel { display: flex; flex: 1; flex-direction: column; min-width: 0; overflow: hidden; }
        .jdm-backdrop.fullscreen .jdm-hero { display: none; }
        .jdm-backdrop.fullscreen .jdm-info-card { display: none; }
        .jdm-backdrop.fullscreen .jdm-sections { margin-top: 0; }
        .jdm-backdrop.fullscreen .jdm-scroll { padding: 20px 28px 0; }
        .jdm-backdrop.fullscreen .jdm-header { border-bottom: 1px solid rgba(200,210,230,0.4); background: #eef1f7; }
        .jdm-handle { width: 36px; height: 4px; background: #c8cedd; border-radius: 99px; margin: 10px auto 0; flex-shrink: 0; }
        @media (min-width: 600px) { .jdm-handle { display: none; } }
        .jdm-header { display: flex; align-items: center; justify-content: space-between; padding: 8px 16px 10px; flex-shrink: 0; background: transparent; }
        .jdm-backdrop.fullscreen .jdm-header { border-bottom: 1px solid rgba(200,210,230,0.4); background: #eef1f7; }
        .jdm-header-left { display: flex; align-items: center; gap: 8px; }
        .jdm-header-label { font-size: 15px; font-weight: 900; color: #1e293b; letter-spacing: -0.2px; }
        .jdm-header-actions { display: flex; align-items: center; gap: 6px; }
        .jdm-back { width: 34px; height: 34px; display: flex; align-items: center; justify-content: center; border-radius: 50%; background: white; border: none; cursor: pointer; color: #334155; box-shadow: 0 1px 6px rgba(0,0,0,0.1); transition: all 0.18s; }
        .jdm-back:hover { background: #f1f5f9; }
        .jdm-back svg { width: 16px; height: 16px; }
        .jdm-expand { display: none; width: 34px; height: 34px; align-items: center; justify-content: center; border-radius: 50%; background: white; border: none; cursor: pointer; color: #334155; box-shadow: 0 1px 6px rgba(0,0,0,0.1); transition: all 0.18s; }
        @media (min-width: 600px) { .jdm-expand { display: flex; } }
        .jdm-expand:hover { background: #f1f5f9; }
        .jdm-expand svg { width: 15px; height: 15px; }
        .jdm-close { width: 34px; height: 34px; display: flex; align-items: center; justify-content: center; border-radius: 50%; background: white; border: none; cursor: pointer; color: #334155; box-shadow: 0 1px 6px rgba(0,0,0,0.1); transition: all 0.18s; }
        .jdm-close:hover { background: #fee2e2; color: #e11d48; transform: rotate(90deg); }
        .jdm-close svg { width: 15px; height: 15px; }
        .jdm-scroll { overflow-y: auto; flex: 1; overscroll-behavior: contain; padding: 0 12px; scroll-behavior: smooth; -webkit-overflow-scrolling: touch; }
        .jdm-scroll::-webkit-scrollbar { width: 3px; }
        .jdm-scroll::-webkit-scrollbar-thumb { background: #c8cedd; border-radius: 20px; }
        .jdm-hero { position: relative; height: 220px; overflow: hidden; border-radius: 20px; flex-shrink: 0; margin-bottom: 2px; }
        .jdm-hero img { width: 100%; height: 100%; object-fit: cover; display: block; }
        .jdm-hero-overlay { position: absolute; inset: 0; background: linear-gradient(to bottom, transparent 50%, rgba(10,14,30,0.5) 100%); }
        .jdm-hero-type { position: absolute; bottom: 12px; right: 12px; background: rgba(15,20,40,0.88); backdrop-filter: blur(10px); color: white; font-size: 11px; font-weight: 900; padding: 4px 12px; border-radius: 99px; border: 1px solid rgba(255,255,255,0.15); max-width: calc(100% - 24px); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
        .jdm-backdrop.fullscreen .jdm-hero { display: none; }
        .jdm-info-card { background: white; border-radius: 20px; padding: 16px 18px; margin-top: 10px; }
        .jdm-backdrop.fullscreen .jdm-info-card { border-radius: 0; margin-top: 0; border-bottom: 1px solid #eef1f7; }
        .jdm-info-location { font-size: 11px; font-weight: 700; color: #94a3b8; margin-bottom: 2px; display: flex; align-items: center; gap: 4px; }
        .jdm-info-location svg { width: 10px; height: 10px; color: #5a5af5; flex-shrink: 0; }
        .jdm-info-company { font-size: 13px; font-weight: 800; color: #1e293b; margin-bottom: 4px; }
        .jdm-info-price { font-size: 26px; font-weight: 900; color: #0f172a; line-height: 1.1; letter-spacing: -0.5px; margin-bottom: 14px; }
        .jdm-backdrop.fullscreen .jdm-info-location, .jdm-backdrop.fullscreen .jdm-info-company, .jdm-backdrop.fullscreen .jdm-info-price { display: none; }
        .jdm-info-chips { display: flex; flex-direction: column; gap: 7px; margin-bottom: 4px; }
        .jdm-backdrop.fullscreen .jdm-info-chips { display: none; }
        .jdm-info-chip { background: #f8fafc; border: 1px solid #e8edf5; border-radius: 10px; padding: 9px 13px; display: flex; align-items: center; gap: 9px; font-size: 13px; font-weight: 700; color: #334155; }
        .jdm-info-chip svg { width: 15px; height: 15px; color: #5a5af5; flex-shrink: 0; }
        .jdm-info-chip span { white-space: normal; word-break: break-word; line-height: 1.3; }
        .jdm-sections { margin-top: 10px; display: flex; flex-direction: column; gap: 10px; padding-bottom: 8px; }
        .jdm-section { background: white; border-radius: 18px; padding: 15px 17px; }
        .jdm-sec-head { display: flex; align-items: center; gap: 9px; margin-bottom: 10px; }
        .jdm-sec-icon { width: 30px; height: 30px; border-radius: 9px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
        .jdm-sec-icon svg { width: 14px; height: 14px; }
        .si2-purple { background: #f3f0ff; color: #7c3aed; }
        .si2-blue   { background: #eff6ff; color: #2563eb; }
        .si2-green  { background: #f0fdf4; color: #16a34a; }
        .si2-amber  { background: #fffbeb; color: #d97706; }
        .si2-indigo { background: #eef2ff; color: #4f46e5; }
        .jdm-sec-title    { font-size: 14px; font-weight: 900; color: #1e293b; }
        .jdm-sec-subtitle { font-size: 10px; font-weight: 700; color: #94a3b8; text-transform: uppercase; letter-spacing: 0.4px; }
        .jdm-para { font-size: 13.5px; color: #374151; line-height: 1.75; font-weight: 500; margin: 0; }
        .jdm-list { margin: 0; padding: 0; list-style: none; display: flex; flex-direction: column; gap: 7px; }
        .jdm-list li { display: flex; align-items: flex-start; gap: 9px; font-size: 13.5px; color: #1e293b; font-weight: 600; line-height: 1.55; }
        .jdm-list li::before { content: ''; width: 6px; height: 6px; border-radius: 50%; background: #5a5af5; flex-shrink: 0; margin-top: 6px; }
        .jdm-list.adv li::before { background: #16a34a; }
        .jdm-list.ben li::before { background: #d97706; }
        .jdm-list.req li::before { background: #2563eb; }
        .jdm-note-box { background: #fffbeb; border: 1.5px solid #fde68a; border-radius: 14px; padding: 12px 16px; display: flex; gap: 10px; align-items: flex-start; }
        .jdm-note-box svg { width: 16px; height: 16px; color: #f59e0b; flex-shrink: 0; margin-top: 1px; }
        .jdm-note-box p { font-size: 13px; color: #78350f; font-weight: 600; line-height: 1.6; margin: 0; }
        .jdm-cta { padding: 14px 14px 22px; flex-shrink: 0; background: rgba(238,241,247,0.97); backdrop-filter: blur(24px); -webkit-backdrop-filter: blur(24px); border-top: 1px solid rgba(180,200,230,0.45); }
        .jdm-apply-btn { width: 100%; padding: 18px 24px; background: linear-gradient(145deg, #d8f54f 0%, #b5e010 100%); color: #1a2000; font-weight: 900; font-size: 16px; letter-spacing: -0.3px; border-radius: 16px; border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 10px; transition: all 0.3s cubic-bezier(0.34,1.5,0.64,1); box-shadow: 0 8px 28px -4px rgba(155,210,10,0.6), inset 0 1px 0 rgba(255,255,255,0.3); position: relative; overflow: hidden; }
        .jdm-apply-btn::before { content: ""; position: absolute; inset: 0; background: linear-gradient(135deg, rgba(255,255,255,0.2) 0%, transparent 60%); border-radius: 16px; pointer-events: none; }
        .jdm-apply-btn:hover { transform: translateY(-4px) scale(1.015); box-shadow: 0 18px 36px -6px rgba(130,180,0,0.65), 0 0 0 5px rgba(212,240,74,0.15); background: linear-gradient(145deg, #ccec38 0%, #a0c800 100%); }
        .jdm-apply-btn:active { transform: translateY(-1px) scale(0.985); }
        .jdm-apply-note { text-align: center; font-size: 11.5px; color: #8a9ab0; font-weight: 600; margin-top: 8px; }
        .jdm-stats { display: none !important; }
        .jdm-stat  { display: none !important; }
        .jdm-badges { display: none; }
        .jdm-hero-co { display: none; }
        .jdm-hero-bottom { display: none; }
        .jdm-title { display: none; }
        .jdm-loc-row { display: none; }
        .jdm-loc { display: none; }
        .si-green  { background: #f0fdf4; color: #16a34a; }
        .si-blue   { background: #eff6ff; color: #2563eb; }
        .si-purple { background: #f3f0ff; color: #7c3aed; }
        .jdm-stat-icon { display: none; }
        .jdm-stat-label { display: none; }
        .jdm-stat-val { display: none; }

        @keyframes slideUpFade{from{opacity:0;transform:translateY(30px)}to{opacity:1;transform:translateY(0)}}
        .animate-card{animation:slideUpFade 0.6s cubic-bezier(0.16,1,0.3,1) forwards;opacity:0;}
        @keyframes shimmer{0%{background-position:-1000px 0}100%{background-position:1000px 0}}
        .skeleton{background:linear-gradient(90deg,#f1f5f9 25%,#e2e8f0 50%,#f1f5f9 75%);background-size:1000px 100%;animation:shimmer 2s infinite;}
        .ticket-separator{position:relative;height:1px;border-top:2px dashed #cbd5e1;margin:0 1.5rem;opacity:0.7;}
        .ticket-separator::before,.ticket-separator::after{content:'';position:absolute;top:-9px;width:18px;height:18px;background-color:#f8fafc;border-radius:50%;box-shadow:inset 0 0 0 1px #e2e8f0;z-index:10;}
        .ticket-separator::before{left:-33px;}.ticket-separator::after{right:-33px;}
        .btn-gradient{background:linear-gradient(135deg,#1e293b 0%,#0f172a 100%);color:white;transition:all 0.3s;position:relative;overflow:hidden;}
        .btn-gradient:hover{background:linear-gradient(135deg,#3b82f6 0%,#2563eb 100%);box-shadow:0 10px 25px -5px rgba(37,99,235,0.4);transform:translateY(-1px);}
        .btn-permanent{background:linear-gradient(135deg,#6366f1 0%,#8b5cf6 100%);color:white;transition:all 0.3s;position:relative;overflow:hidden;}
        .btn-permanent:hover{background:linear-gradient(135deg,#4f46e5 0%,#7c3aed 100%);box-shadow:0 10px 25px -5px rgba(99,102,241,0.4);transform:translateY(-2px);}
        .btn-permanent::before{content:'';position:absolute;top:50%;left:50%;width:0;height:0;border-radius:50%;background:rgba(255,255,255,0.3);transform:translate(-50%,-50%);transition:width 0.6s,height 0.6s;}
        .btn-permanent:hover::before{width:300px;height:300px;}
        .scrollbar-hide::-webkit-scrollbar{display:none;}
        .scrollbar-hide{-ms-overflow-style:none;scrollbar-width:none;}
        .toggle-switch{position:relative;display:inline-block;width:100%;background:#f1f5f9;border-radius:16px;padding:4px;display:flex;gap:4px;}
        .toggle-option{flex:1;padding:12px 20px;text-align:center;font-weight:700;font-size:14px;border-radius:12px;cursor:pointer;transition:all 0.3s;color:#64748b;}
        .toggle-option.active{background:white;color:#1e293b;box-shadow:0 2px 8px rgba(0,0,0,0.08);}
        .custom-scrollbar::-webkit-scrollbar{width:6px;}
        .custom-scrollbar::-webkit-scrollbar-track{background:#f1f5f9;}
        .custom-scrollbar::-webkit-scrollbar-thumb{background-color:#cbd5e1;border-radius:20px;}
        @keyframes modalFadeIn{from{opacity:0;transform:scale(0.95) translateY(10px)}to{opacity:1;transform:scale(1) translateY(0)}}
        .animate-modal{animation:modalFadeIn 0.3s cubic-bezier(0.16,1,0.3,1) forwards;}
        @keyframes contactSlideUp{from{opacity:0;transform:scale(0.95) translateY(16px)}to{opacity:1;transform:scale(1) translateY(0)}}
        .animate-contact-modal{animation:contactSlideUp 0.35s cubic-bezier(0.16,1,0.3,1) forwards;}
        .contact-subject-chip{display:inline-flex;align-items:center;gap:6px;border:1.5px solid #e2e8f0;border-radius:10px;padding:8px 14px;font-size:13px;font-weight:700;color:#475569;background:#f8fafc;cursor:pointer;transition:all 0.2s;user-select:none;}
        .contact-subject-chip:hover{border-color:#93c5fd;background:#eff6ff;color:#1d4ed8;}
        .contact-subject-chip.selected{border-color:#3b82f6;background:#eff6ff;color:#1d4ed8;box-shadow:0 0 0 3px rgba(59,130,246,0.12);}
        .contact-subject-chip svg{width:14px;height:14px;flex-shrink:0;}

        /* ══ CTA CARD - Promote your job ══ */
        .re-card-cta { background: linear-gradient(135deg, #f0f4ff 0%, #ebe8ff 50%, #f4f0ff 100%); border: 2px dashed #a5b4fc !important; }
        .re-card-cta:hover { transform: translateY(-8px); box-shadow: 0 20px 50px -12px rgba(99,102,241,0.25) !important; }
        .cta-hero-bg { height: 190px; background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #a78bfa 100%); border-radius: 18px; margin: 10px 10px 0; display: flex; align-items: center; justify-content: center; overflow: hidden; position: relative; flex-shrink: 0; }
        .cta-hero-circle { position: absolute; border-radius: 50%; background: rgba(255,255,255,0.1); }
        .cta-book-btn { width: 100%; padding: 13px 16px; background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%); color: white; font-weight: 900; font-size: 14px; border-radius: 13px; border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 7px; transition: all 0.22s; box-shadow: 0 3px 14px -3px rgba(99,102,241,0.55); white-space: nowrap; overflow: hidden; }
        .cta-book-btn:hover { background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%); transform: translateY(-2px); box-shadow: 0 8px 22px -4px rgba(99,102,241,0.6); }

        /* ══ SHARE POPUP ══ */
        .share-popup-backdrop { display:none; position:fixed; inset:0; background:rgba(8,10,24,0.55); backdrop-filter:blur(8px); z-index:150; align-items:flex-end; justify-content:center; }
        .share-popup-backdrop.active { display:flex; }
        @media(min-width:600px){ .share-popup-backdrop { align-items:center; } }
        .share-popup { background:white; width:100%; max-width:420px; border-radius:28px 28px 0 0; padding:0 0 28px; animation:jdmSlideUp 0.38s cubic-bezier(0.34,1.2,0.64,1) forwards; }
        @media(min-width:600px){ .share-popup { border-radius:24px; animation:jdmFadeScale 0.32s cubic-bezier(0.34,1.2,0.64,1) forwards; } }
        .share-handle { width:36px;height:4px;background:#e2e8f0;border-radius:99px;margin:12px auto 0; }
        @media(min-width:600px){.share-handle{display:none;}}
        .share-header { display:flex; align-items:center; justify-content:space-between; padding:14px 20px 10px; border-bottom:1px solid #f1f5f9; }
        .share-title { font-size:16px; font-weight:900; color:#0f172a; }
        .share-close { width:30px;height:30px;border-radius:50%;background:#f1f5f9;border:none;cursor:pointer;display:flex;align-items:center;justify-content:center;color:#64748b;transition:all 0.18s; }
        .share-close:hover { background:#fee2e2;color:#e11d48;transform:rotate(90deg); }
        .share-close svg { width:13px;height:13px; }
        .share-job-info { margin:12px 20px; background:#f8fafc; border:1px solid #e8edf5; border-radius:14px; padding:12px 14px; }
        .share-job-info-title { font-size:14px;font-weight:900;color:#1e293b;margin-bottom:2px; }
        .share-job-info-sub { font-size:11.5px;font-weight:700;color:#94a3b8; }
        .share-btns { display:flex; flex-direction:column; gap:8px; padding:10px 20px 0; }
        .share-btn { display:flex; align-items:center; gap:13px; padding:14px 16px; border-radius:16px; border:1.5px solid #e8edf5; background:white; cursor:pointer; transition:all 0.2s; text-align:right; }
        .share-btn:hover { border-color:#93c5fd; background:#f0f7ff; transform:translateX(-2px); }
        .share-btn-icon { width:42px;height:42px;border-radius:12px;display:flex;align-items:center;justify-content:center;flex-shrink:0; }
        .share-btn-icon svg { width:20px;height:20px; }
        .share-btn-label { font-size:14px;font-weight:800;color:#1e293b; }
        .share-btn-sub { font-size:11px;font-weight:600;color:#94a3b8;margin-top:1px; }
        .share-divider { display:flex;align-items:center;gap:10px;padding:4px 20px; }
        .share-divider-line { flex:1;height:1px;background:#f1f5f9; }
        .share-divider-text { font-size:11px;font-weight:700;color:#94a3b8;white-space:nowrap; }
        .share-link-row { display:flex; gap:8px; padding:6px 20px 0; }
        .share-link-input { flex:1;background:#f8fafc;border:1.5px solid #e8edf5;border-radius:12px;padding:10px 14px;font-size:12px;font-weight:700;color:#475569;outline:none;overflow:hidden;text-overflow:ellipsis;white-space:nowrap; }
        .share-copy-btn { flex-shrink:0;padding:10px 16px;background:#0f172a;color:white;border:none;border-radius:12px;font-size:12px;font-weight:800;cursor:pointer;transition:all 0.2s;white-space:nowrap; }
        .share-copy-btn:hover { background:#3b82f6; }
        .share-copy-btn.copied { background:#16a34a; }

        /* ══ EMPLOYER MODAL ══ */
        .employer-modal-backdrop { display:none; position:fixed; inset:0; background:rgba(8,10,24,0.6); backdrop-filter:blur(10px); z-index:150; align-items:flex-end; justify-content:center; }
        .employer-modal-backdrop.active { display:flex; }
        @media(min-width:600px){ .employer-modal-backdrop { align-items:center; padding:24px; } }
        .employer-modal { background:white; width:100%; max-width:520px; border-radius:28px 28px 0 0; max-height:92dvh; display:flex; flex-direction:column; overflow:hidden; animation:jdmSlideUp 0.4s cubic-bezier(0.34,1.2,0.64,1) forwards; }
        @media(min-width:600px){ .employer-modal { border-radius:24px; animation:jdmFadeScale 0.36s cubic-bezier(0.34,1.2,0.64,1) forwards; } }
        .employer-modal-hero { background:linear-gradient(135deg,#6366f1 0%,#8b5cf6 60%,#a78bfa 100%); padding:24px 24px 28px; position:relative; overflow:hidden; flex-shrink:0; }
        .employer-modal-hero::before { content:''; position:absolute; top:-40px; right:-40px; width:160px; height:160px; border-radius:50%; background:rgba(255,255,255,0.08); }
        .employer-modal-hero::after  { content:''; position:absolute; bottom:-30px; left:-20px; width:110px; height:110px; border-radius:50%; background:rgba(255,255,255,0.06); }
        .employer-modal-scroll { overflow-y:auto; flex:1; padding:20px 22px; }
        .employer-modal-scroll::-webkit-scrollbar { width:3px; }
        .employer-modal-scroll::-webkit-scrollbar-thumb { background:#e2e8f0; border-radius:20px; }
        .employer-field { margin-bottom:14px; }
        .employer-field label { display:block; font-size:11.5px; font-weight:800; color:#64748b; margin-bottom:6px; text-transform:uppercase; letter-spacing:0.4px; }
        .employer-field label span { color:#e11d48; }
        .employer-input { width:100%; background:#f8fafc; border:1.5px solid #e2e8f0; color:#1e293b; font-size:13.5px; font-weight:600; border-radius:13px; padding:11px 14px; outline:none; transition:all 0.18s; font-family:inherit; }
        .employer-input:focus { border-color:#a5b4fc; background:white; box-shadow:0 0 0 3px rgba(165,180,252,0.2); }
        .employer-select { appearance:none; cursor:pointer; background-image:url("data:image/svg+xml,%3Csvg fill='none' viewBox='0 0 24 24' stroke='%2394a3b8' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M19 9l-7 7-7-7'/%3E%3C/svg%3E"); background-repeat:no-repeat; background-position:left 12px center; background-size:16px; padding-left:36px; }
        .employer-chips-row { display:flex; flex-wrap:wrap; gap:7px; }
        .employer-chip { display:inline-flex; align-items:center; gap:5px; padding:7px 13px; border:1.5px solid #e2e8f0; border-radius:10px; font-size:12.5px; font-weight:700; color:#475569; background:#f8fafc; cursor:pointer; transition:all 0.18s; user-select:none; }
        .employer-chip:hover { border-color:#a5b4fc; color:#4f46e5; }
        .employer-chip.selected { border-color:#6366f1; background:#eef2ff; color:#4f46e5; box-shadow:0 0 0 3px rgba(99,102,241,0.12); }
        .employer-submit-btn { width:100%; padding:16px; background:linear-gradient(135deg,#6366f1,#8b5cf6); color:white; font-weight:900; font-size:15px; border-radius:14px; border:none; cursor:pointer; display:flex; align-items:center; justify-content:center; gap:8px; transition:all 0.25s; box-shadow:0 6px 20px -4px rgba(99,102,241,0.5); margin-top:4px; }
        .employer-submit-btn:hover { background:linear-gradient(135deg,#4f46e5,#7c3aed); transform:translateY(-2px); box-shadow:0 12px 28px -6px rgba(99,102,241,0.6); }

        /* ══ FUZZY SEARCH ══ */
        .fuzzy-suggestion { display:none; position:absolute; top:calc(100% + 6px); right:0; left:0; background:white; border:1.5px solid #e0e7ff; border-radius:14px; padding:10px 14px; box-shadow:0 8px 24px -6px rgba(99,102,241,0.2); z-index:30; }
        .fuzzy-suggestion.visible { display:flex; align-items:center; gap:8px; }
        .fuzzy-suggestion-text { font-size:13px; font-weight:600; color:#475569; flex:1; }
        .fuzzy-suggestion-text strong { color:#4f46e5; font-weight:800; }
        .fuzzy-suggestion-accept { padding:5px 12px; background:#6366f1; color:white; border:none; border-radius:8px; font-size:12px; font-weight:800; cursor:pointer; white-space:nowrap; transition:background 0.18s; }
        .fuzzy-suggestion-accept:hover { background:#4f46e5; }
        .fuzzy-suggestion-dismiss { width:22px;height:22px;border-radius:50%;background:#f1f5f9;border:none;cursor:pointer;display:flex;align-items:center;justify-content:center;color:#94a3b8;flex-shrink:0;font-size:14px;line-height:1;transition:all 0.18s; }
        .fuzzy-suggestion-dismiss:hover { background:#fee2e2;color:#e11d48; }
    </style>
</head>
<body class="antialiased text-slate-600 min-h-screen flex flex-col font-sans">

    <!-- ANNOUNCEMENT STRIP -->
    <div id="announcementStrip">
        <div class="strip-accent-line"></div>
        <div class="strip-single" id="stripSingle" style="display:none">
            <div class="strip-single-icon" id="stripSingleIcon">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" id="stripSingleIconSvg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
            </div>
            <span class="strip-single-label" id="stripSingleLabel">עדכון</span>
            <div class="strip-single-sep"></div>
            <span class="strip-single-text" id="stripSingleText"></span>
            <span class="strip-single-date" id="stripSingleDate"></span>
            <button class="strip-single-cta" onclick="openPanel()">
                לפרטים
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M15 19l-7-7 7-7"/></svg>
            </button>
        </div>
        <div class="strip-inner" id="stripMulti" style="display:none">
            <div class="strip-label-pill">
                <div class="strip-type-dot"></div>
                <span class="strip-type-text" id="stripTypeText">עדכון</span>
                <span class="strip-count-badge" id="stripCountBadge">1</span>
            </div>
            <div class="strip-fade-wrap" id="stripFadeWrap"></div>
            <div class="strip-dots" id="stripDots"></div>
            <button class="strip-cta" onclick="openPanel()">
                <span class="strip-cta-text">כל העדכונים</span>
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M15 19l-7-7 7-7"/></svg>
            </button>
        </div>
    </div>

    <!-- NAV -->
    <nav class="glass sticky top-0 z-40">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-20 flex items-center justify-between">
            <div class="flex items-center gap-3">
                <div class="bg-gradient-to-br from-blue-600 to-blue-700 text-white p-2.5 rounded-xl shadow-lg shadow-blue-500/20">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" /></svg>
                </div>
                <div class="leading-none select-none">
                    <h1 class="text-2xl font-black text-slate-800 tracking-tight">CHEETAH<span class="text-blue-600">JOBS</span></h1>
                    <span class="text-[10px] font-bold text-slate-400 tracking-widest uppercase ml-0.5">מערכת שיבוץ</span>
                </div>
            </div>
            <div class="hidden sm:flex items-center gap-2 bg-white px-4 py-1.5 rounded-full border border-slate-200 shadow-sm">
                <span class="relative flex h-2.5 w-2.5"><span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span><span class="relative inline-flex rounded-full h-2.5 w-2.5 bg-emerald-500"></span></span>
                <span class="text-xs font-bold text-slate-700">מערכת פעילה</span>
            </div>
        </div>
    </nav>

    <main class="flex-grow max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 w-full">
        <div class="text-center mb-10">
            <h2 class="text-4xl md:text-5xl font-black text-slate-900 mb-3 tracking-tight">לוח <span id="jobTypeTitle" class="text-transparent bg-clip-text bg-gradient-to-r from-blue-600 to-indigo-600">משמרות</span></h2>
            <p class="text-slate-500 font-medium text-lg max-w-2xl mx-auto" id="jobTypeSubtitle">מצאו את המשמרת הבאה שלכם, הירשמו בקליק והתחילו לעבוד.</p>
        </div>
        <div class="max-w-md mx-auto mb-8">
            <div class="toggle-switch">
                <div class="toggle-option active" id="tempToggle" onclick="switchJobType('temporary')">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 inline-block ml-1" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                    משמרות מזדמנות
                </div>
                <div class="toggle-option" id="permToggle" onclick="switchJobType('permanent')">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 inline-block ml-1" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" /></svg>
                    משרות קבועות
                </div>
            </div>
        </div>
        <div class="bg-white rounded-2xl shadow-soft border border-slate-100 p-4 mb-10 transition-all duration-300">
            <div class="flex gap-3 items-center mb-0 md:mb-4">
                <div class="relative flex-grow z-10">
                    <div class="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none"><svg class="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path></svg></div>
                    <input type="text" id="searchInput" onkeyup="applyFilters()" autocomplete="off" class="w-full pl-4 pr-10 py-3 rounded-xl bg-slate-50 border border-slate-200 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all font-medium" placeholder="חפש...">
                    <div class="fuzzy-suggestion" id="fuzzySuggestion">
                        <svg width="16" height="16" fill="none" viewBox="0 0 24 24" stroke="#6366f1" stroke-width="2" style="flex-shrink:0"><path stroke-linecap="round" stroke-linejoin="round" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"/></svg>
                        <span class="fuzzy-suggestion-text" id="fuzzySuggestionText"></span>
                        <button class="fuzzy-suggestion-accept" id="fuzzySuggestionAccept">חפש</button>
                        <button class="fuzzy-suggestion-dismiss" onclick="document.getElementById('fuzzySuggestion').classList.remove('visible')">✕</button>
                    </div>
                </div>
                <button onclick="toggleFilters()" class="md:hidden shrink-0 bg-blue-50 text-blue-600 border border-blue-100 p-3 rounded-xl active:scale-95 transition-transform">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4" /></svg>
                </button>
            </div>
            <div id="advancedFilters" class="hidden md:grid md:grid-cols-3 gap-4 border-t md:border-t-0 border-slate-100 pt-4 md:pt-0 mt-4 md:mt-0">
                <div class="relative">
                    <div class="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none"><svg class="w-4 h-4 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path></svg></div>
                    <input type="text" id="locationFilter" onkeyup="applyFilters()" class="w-full pl-4 pr-10 py-3 rounded-xl bg-slate-50 border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none text-sm" placeholder="סינון לפי מיקום">
                </div>
                <div class="relative">
                    <div class="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none"><svg class="w-4 h-4 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg></div>
                    <input type="number" id="rateFilter" onkeyup="applyFilters()" class="w-full pl-4 pr-10 py-3 rounded-xl bg-slate-50 border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none text-sm" placeholder="שכר מינימום לשעה">
                </div>
                <div class="relative">
                    <select id="ageFilter" onchange="applyFilters()" class="w-full pl-4 pr-10 py-3 rounded-xl bg-slate-50 border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none text-sm appearance-none cursor-pointer text-slate-500">
                        <option value="">הצג הכל (ללא הגבלת גיל)</option>
                        <option value="16">אני בן 16+</option>
                        <option value="17">אני בן 17+</option>
                        <option value="18">אני בן 18+</option>
                        <option value="21">אני בן 21+</option>
                    </select>
                    <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none"><svg class="w-4 h-4 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg></div>
                </div>
            </div>
            <div class="flex justify-between items-center mt-3 pt-3 border-t border-slate-100">
                <div class="text-xs font-bold text-slate-400" id="resultsCount">מציג את כל המשמרות</div>
                <button onclick="clearFilters()" class="text-xs font-bold text-blue-600 hover:text-blue-800 transition-colors">נקה סינון</button>
            </div>
        </div>
        <div id="shifts-container" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 pb-10"></div>
    </main>

    <footer class="mt-auto bg-white border-t border-slate-100 shadow-[0_-4px_6px_-1px_rgba(0,0,0,0.02)]">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div class="flex flex-col md:flex-row items-center justify-between gap-6">
                <div class="text-center md:text-right">
                    <p class="text-slate-900 font-bold text-lg tracking-tight">Cheetah Jobs</p>
                    <p class="text-slate-500 text-sm mt-1">&copy; 2026 Cheetah Jobs System. כל הזכויות שמורות.</p>
                </div>
                <div class="flex items-center gap-6">
                    <button onclick="openTerms()" class="group flex items-center gap-2 text-sm font-medium text-slate-600 hover:text-blue-600 transition-colors focus:outline-none">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-slate-400 group-hover:text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" /></svg>
                        תקנון האתר
                    </button>
                    <span class="h-4 w-px bg-slate-200"></span>
                    <button onclick="openContact()" class="group flex items-center gap-2 text-sm font-medium text-slate-600 hover:text-blue-600 transition-colors focus:outline-none">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-slate-400 group-hover:text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" /></svg>
                        צור קשר
                    </button>
                </div>
            </div>
        </div>
    </footer>

    <!-- FLOATING BELL -->
    <button id="announcementBtn" onclick="openPanel()" aria-label="עדכונים">
        <div id="announcementBadge"></div>
        <svg class="bell-icon" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"/>
        </svg>
        <span class="bell-label">עדכונים</span>
    </button>

    <div id="panelBackdrop" onclick="closePanel()"></div>

    <!-- ANNOUNCEMENTS PANEL -->
    <div id="announcementPanel">
        <div class="panel-handle"></div>
        <div class="panel-header">
            <div class="panel-header-left">
                <div class="panel-header-icon">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5.882V19.24a1.76 1.76 0 01-3.417.592l-2.147-6.15M18 13a3 3 0 100-6M5.436 13.683A4.001 4.001 0 017 6h1.832c4.1 0 7.625-1.234 9.168-3v14c-1.543-1.766-5.067-3-9.168-3H7a3.988 3.988 0 01-1.564-.317z"/></svg>
                </div>
                <div>
                    <div class="panel-header-title">עדכונים ×<span id="panelCount">0</span></div>
                    <div class="panel-header-sub">הודעות מהצוות</div>
                </div>
            </div>
            <button class="panel-close" onclick="closePanel()">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12"/></svg>
            </button>
        </div>
        <div class="panel-tabs-wrapper" id="panelTabsWrapper">
            <button class="tabs-arrow arrow-right" onclick="scrollTabs(80)">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M9 5l7 7-7 7"/></svg>
            </button>
            <button class="tabs-arrow arrow-left" onclick="scrollTabs(-80)">
                <svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M15 19l-7-7 7-7"/></svg>
            </button>
            <div class="panel-tabs" id="panelTabs">
                <button class="panel-tab active" data-filter="all" onclick="filterPanel(this,'all')"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 10h16M4 14h16M4 18h16"/></svg>הכל</button>
                <button class="panel-tab" data-filter="urgent" onclick="filterPanel(this,'urgent')"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/></svg>דחוף</button>
                <button class="panel-tab" data-filter="info" onclick="filterPanel(this,'info')"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>כללי</button>
                <button class="panel-tab" data-filter="new" onclick="filterPanel(this,'new')"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z"/></svg>חדש</button>
                <button class="panel-tab" data-filter="warning" onclick="filterPanel(this,'warning')"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/></svg>תחזוקה</button>
                <button class="panel-tab" data-filter="security" onclick="filterPanel(this,'security')"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/></svg>ביטחוני</button>
            </div>
        </div>
        <div class="panel-scroll" id="panelList"></div>
    </div>

    <!-- TERMS MODAL -->
    <div id="termsModal" class="fixed inset-0 z-[100] hidden" role="dialog" aria-modal="true">
        <div class="fixed inset-0 bg-slate-900/40 backdrop-blur-md" onclick="closeTerms()"></div>
        <div class="fixed inset-0 z-10 overflow-y-auto">
            <div class="flex min-h-full items-center justify-center p-4 sm:p-0">
                <div class="relative transform overflow-hidden rounded-2xl bg-white text-right shadow-2xl sm:my-8 w-full sm:max-w-3xl animate-modal border border-slate-100">
                    <div class="bg-white/90 backdrop-blur sticky top-0 z-20 px-6 py-5 border-b border-slate-100 flex justify-between items-center">
                        <div><h3 class="text-xl font-black text-slate-800 flex items-center gap-2"><span class="bg-blue-100 text-blue-600 p-1.5 rounded-lg"><svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" /></svg></span>תקנון ותנאי שימוש</h3><p class="text-xs text-slate-500 mt-1 pr-11">עודכן לאחרונה: פברואר 2026</p></div>
                        <button onclick="closeTerms()" class="group p-2 rounded-full hover:bg-slate-100 transition-colors focus:outline-none"><svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-slate-400 group-hover:text-slate-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg></button>
                    </div>
                    <div class="px-6 py-8 max-h-[60vh] overflow-y-auto custom-scrollbar space-y-8">
                        <section class="relative pl-2"><h4 class="flex items-center gap-2 font-bold text-slate-900 mb-2"><span class="text-blue-600">01.</span> מהות האתר</h4><p class="text-slate-600 text-sm leading-relaxed">אתר Cheetah Jobs משמש כפלטפורמה טכנולוגית לתווך וחיבור בין מחפשי עבודה למעסיקים.<span class="block mt-2 font-medium text-slate-800 bg-slate-50 p-2 rounded border border-slate-100">הנהלת האתר משמשת כמתווך בלבד ואינה צד ליחסי העבודה.</span></p></section>
                        <section><h4 class="flex items-center gap-2 font-bold text-slate-900 mb-2"><span class="text-blue-600">02.</span> יחסי עובד-מעביד</h4><p class="text-slate-600 text-sm leading-relaxed">השימוש באתר אינו יוצר יחסי עובד-מעביד מול האתר. המעסיק בפועל הוא האחראי הבלעדי לתשלום שכר, הנפקת תלוש, ושמירה על זכויות סוציאליות ובטיחות.</p></section>
                        <section><h4 class="flex items-center gap-2 font-bold text-slate-900 mb-2"><span class="text-blue-600">03.</span> הרשמה והתחייבות</h4><p class="text-slate-600 text-sm leading-relaxed">אי-הגעה למשמרת ("No Show") או ביטול ברגע האחרון ללא סיבה מוצדקת פוגעים באמינותך ועלולים לגרור <strong class="text-rose-500">חסימה לצמיתות</strong> מהפלטפורמה.</p></section>
                        <section><h4 class="flex items-center gap-2 font-bold text-slate-900 mb-2"><span class="text-blue-600">04.</span> שכר ותשלומים</h4><div class="flex items-start gap-3 bg-amber-50 border border-amber-100 rounded-xl p-4"><svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-amber-500 flex-shrink-0 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" /></svg><p class="text-xs text-amber-800 font-medium leading-relaxed">התשלום מתבצע ע"י המעסיק בפועל ב-10 לחודש העוקב.</p></div></section>
                        <section><h4 class="flex items-center gap-2 font-bold text-slate-900 mb-2"><span class="text-blue-600">05.</span> פרטיות ומידע</h4><p class="text-slate-600 text-sm leading-relaxed">אישורך מהווה הסכמה להעברת פרטייך (שם, ת"ז, טלפון) למעסיק הרלוונטי לצורך שיבוץ והפקת שכר.</p></section>
                    </div>
                    <div class="p-4 sm:px-6 bg-slate-50 border-t border-slate-100"><button onclick="closeTerms()" class="w-full py-3.5 px-4 bg-gradient-to-r from-slate-800 to-slate-900 hover:from-slate-700 hover:to-slate-800 text-white font-bold rounded-xl shadow-lg shadow-slate-200 transform hover:-translate-y-0.5 transition-all duration-200 flex items-center justify-center gap-2"><span>קראתי ואני מאשר/ת</span><svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" /></svg></button></div>
                </div>
            </div>
        </div>
    </div>

    <!-- CONTACT MODAL -->
    <div id="contactModal" class="fixed inset-0 z-[100] hidden" role="dialog" aria-modal="true">
        <div class="fixed inset-0 bg-slate-900/50 backdrop-blur-md" onclick="closeContact()"></div>
        <div class="fixed inset-0 z-10 overflow-y-auto">
            <div class="flex min-h-full items-center justify-center p-4">
                <div class="relative w-full max-w-xl bg-white rounded-[26px] shadow-2xl border border-slate-100 overflow-hidden animate-contact-modal">
                    <div class="bg-gradient-to-l from-blue-600 to-indigo-700 px-6 pt-6 pb-16 relative overflow-hidden">
                        <div class="absolute inset-0 opacity-10 pointer-events-none"><div class="absolute -top-8 -left-8 w-40 h-40 rounded-full bg-white"></div><div class="absolute -bottom-12 -right-6 w-56 h-56 rounded-full bg-white"></div></div>
                        <div class="flex justify-between items-start relative z-10"><div><h3 class="text-2xl font-black text-white">צור קשר</h3><p class="text-blue-100 text-sm font-medium mt-1">נשמח לשמוע ממך ולעזור</p></div><button onclick="closeContact()" class="w-9 h-9 flex items-center justify-center rounded-full bg-white/15 border border-white/25 text-white hover:bg-white/25 transition-colors"><svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg></button></div>
                        <a href="/cdn-cgi/l/email-protection#4a202528392925222f247b0a2d272b232664292527" class="relative z-10 mt-5 inline-flex items-center gap-2.5 bg-white/15 border border-white/25 rounded-xl px-4 py-2.5 text-white text-sm font-bold hover:bg-white/25 transition-colors"><svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-blue-200" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" /></svg><span class="__cf_email__" data-cfemail="b8d2d7dacbdbd7d0ddd689f8dfd5d9d1d496dbd7d5">[email&#160;protected]</span></a>
                    </div>
                    <div class="relative -mt-8 mx-4 mb-4 bg-white rounded-2xl border border-slate-100 shadow-lg p-5">
                        <p class="text-xs font-bold text-slate-400 uppercase tracking-wider mb-4">או שלח/י לנו הודעה ישירה</p>
                        <form id="contactForm" class="space-y-4">
                            <div>
                                <label class="block text-xs font-bold text-slate-500 mb-2">נושא הפנייה <span class="text-rose-400">*</span></label>
                                <div class="flex flex-wrap gap-2" id="subjectChips">
                                    <div class="contact-subject-chip" data-value="שאלה על משמרת" onclick="selectSubject(this)"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>שאלה על משמרת</div>
                                    <div class="contact-subject-chip" data-value="בעיה בהרשמה" onclick="selectSubject(this)"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/></svg>בעיה בהרשמה</div>
                                    <div class="contact-subject-chip" data-value="שכר ותשלומים" onclick="selectSubject(this)"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>שכר ותשלומים</div>
                                    <div class="contact-subject-chip" data-value="ביטול משמרת" onclick="selectSubject(this)"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>ביטול משמרת</div>
                                    <div class="contact-subject-chip" data-value="הצעת שיתוף פעולה" onclick="selectSubject(this)"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z"/></svg>הצעת שיתוף פעולה</div>
                                    <div class="contact-subject-chip" data-value="אחר" onclick="selectSubject(this)"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"/></svg>אחר</div>
                                </div>
                                <input type="hidden" id="contactSubjectInput" name="subject">
                            </div>
                            <div class="grid grid-cols-2 gap-3">
                                <div><label class="block text-xs font-bold text-slate-500 mb-1.5">שם מלא <span class="text-rose-400">*</span></label><input type="text" name="full_name" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-blue-500 focus:border-blue-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="ישראל ישראלי"></div>
                                <div><label class="block text-xs font-bold text-slate-500 mb-1.5">טלפון</label><input type="tel" name="phone" maxlength="10" class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-blue-500 focus:border-blue-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="050-0000000"></div>
                            </div>
                            <div><label class="block text-xs font-bold text-slate-500 mb-1.5">אימייל <span class="text-rose-400">*</span></label><input type="email" name="email" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-blue-500 focus:border-blue-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="your@email.com"></div>
                            <div><label class="block text-xs font-bold text-slate-500 mb-1.5">הודעה <span class="text-rose-400">*</span></label><textarea name="message" required rows="4" class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-blue-500 focus:border-blue-500 block p-3 outline-none transition-all placeholder-slate-400 resize-none" placeholder="כתוב/י את פנייתך כאן..."></textarea></div>
                            <button type="submit" id="contactSubmitBtn" class="w-full py-3.5 bg-gradient-to-l from-blue-600 to-indigo-700 hover:from-blue-700 hover:to-indigo-800 text-white font-bold rounded-xl shadow-lg shadow-blue-200 transform hover:-translate-y-0.5 transition-all duration-200 flex items-center justify-center gap-2"><svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" /></svg>שלח הודעה</button>
                            <p class="text-center text-[11px] text-slate-400">נחזור אליך בהקדם האפשרי</p>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- JOB DETAILS MODAL -->
    <div id="jobDetailsModal" class="jdm-backdrop" onclick="if(event.target===this)closeJobModal()">
        <div class="jdm-container">
            <div class="jdm-left-panel" id="jdm-left-panel">
                <div class="jdm-left-panel-hero">
                    <img id="jdm-left-img" src="" alt="">
                    <div class="jdm-left-panel-overlay"></div>
                </div>
                <div class="jdm-left-panel-info">
                    <div class="jdm-left-panel-type" id="jdm-left-type"></div>
                    <div class="jdm-left-panel-title" id="jdm-left-title"></div>
                    <div class="jdm-left-panel-company" id="jdm-left-company"></div>
                    <div class="jdm-left-panel-salary" id="jdm-left-salary"></div>
                    <div class="jdm-left-panel-location" id="jdm-left-location"></div>
                    <div class="jdm-left-panel-chips" id="jdm-left-chips"></div>
                </div>
            </div>
            <div class="jdm-right-panel">
                <div class="jdm-handle"></div>
                <div class="jdm-header">
                    <div class="jdm-header-left">
                        <button class="jdm-back" onclick="closeJobModal()" title="חזור">
                            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M9 5l7 7-7 7"/></svg>
                        </button>
                        <span class="jdm-header-label">פרטי המשרה</span>
                    </div>
                    <div class="jdm-header-actions">
                        <button class="jdm-expand" onclick="toggleJobModalFullscreen()" title="מסך מלא">
                            <svg id="jdmExpandIcon" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8V4m0 0h4M4 4l5 5m11-1V4m0 0h-4m4 0l-5 5M4 16v4m0 0h4m-4 0l5-5m11 5l-5-5m5 5v-4m0 4h-4"/>
                            </svg>
                        </button>
                        <button class="jdm-close" onclick="closeJobModal()">
                            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12"/></svg>
                        </button>
                    </div>
                </div>
                <div class="jdm-scroll" id="jdm-scroll">
                    <div id="jdm-content"></div>
                </div>
                <div class="jdm-cta" id="jdm-cta-fixed" style="display:none">
                    <button class="jdm-apply-btn" id="jdm-cta-btn">הגש מועמדות <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" style="width:1.1rem;height:1.1rem;flex-shrink:0;display:inline"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"/></svg></button>
                    <p class="jdm-apply-note">תקבל מענה תוך 3-5 ימי עסקים</p>
                </div>
            </div>
        </div>
    </div>

    <!-- TEMPORARY SHIFT MODAL -->
    <div id="modal" class="fixed inset-0 bg-slate-900/60 backdrop-blur-sm z-50 hidden flex items-center justify-center p-4">
        <div class="bg-white w-full max-w-md rounded-[28px] shadow-2xl flex flex-col max-h-[95vh] overflow-hidden relative">
            <div class="bg-white p-6 pb-2 relative z-10">
                <div class="flex justify-between items-start mb-6"><div><h3 class="text-2xl font-black text-slate-800">אישור שיבוץ</h3><p class="text-slate-500 text-xs font-bold tracking-wide mt-1">טופס הזמנה #<span id="random-order-id">2930</span></p></div><button onclick="closeModal()" class="w-9 h-9 flex items-center justify-center rounded-full bg-slate-50 border border-slate-200 text-slate-400 hover:bg-rose-50 hover:text-rose-500 hover:border-rose-200 transition-colors"><svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg></button></div>
                <div class="bg-blue-50 border border-blue-100 rounded-2xl p-4 flex items-center gap-4"><div class="bg-white p-2.5 rounded-xl text-blue-600 shrink-0 shadow-sm border border-blue-50"><svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg></div><div><div class="text-[10px] font-bold text-blue-400 uppercase tracking-wider mb-0.5">פרטי המשמרת הנבחרת</div><div id="modal-shift-info" class="font-bold text-slate-800 text-sm leading-tight"></div></div></div>
            </div>
            <div class="overflow-y-auto p-6 pt-2 scrollbar-hide bg-white flex-1">
                <form id="regForm" class="space-y-4">
                    <input type="hidden" name="shift_details" id="shift_details">
                    <input type="hidden" name="shift_hours" id="shift_hours_input">
                    <input type="hidden" name="shift_date" id="shift_date_input">
                    <input type="hidden" name="company" id="company_input">
                    <div class="grid grid-cols-2 gap-4"><div><label class="block text-xs font-bold text-slate-500 mb-1.5">שם פרטי</label><input type="text" name="fname" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-blue-500 focus:border-blue-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="צ׳יטה"></div><div><label class="block text-xs font-bold text-slate-500 mb-1.5">שם משפחה</label><input type="text" name="lname" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-blue-500 focus:border-blue-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="ג׳ובס"></div></div>
                    <div><label class="block text-xs font-bold text-slate-500 mb-1.5">תעודת זהות</label><input type="tel" id="idInput" name="id_num" maxlength="9" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-blue-500 focus:border-blue-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="000000000"></div>
                    <div class="bg-emerald-50 border border-emerald-100 rounded-xl p-3 flex gap-3 items-center"><div class="bg-emerald-100 p-1.5 rounded-full text-emerald-600 shrink-0"><svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg></div><span class="text-xs font-bold text-emerald-800 leading-tight">השכר משולם ב-10 לכל חודש כנגד תלוש שכר כחוק</span></div>
                    <div class="grid grid-cols-2 gap-4"><div><label class="block text-xs font-bold text-slate-500 mb-1.5">טלפון נייד</label><input type="tel" id="phoneInput" name="phone" required maxlength="10" class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-blue-500 focus:border-blue-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="050-0000000"></div><div><label class="block text-xs font-bold text-slate-500 mb-1.5">תאריך לידה</label><input type="date" name="dob" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-blue-500 focus:border-blue-500 block p-3 outline-none transition-all text-slate-500"></div></div>
                    <div><label class="block text-xs font-bold text-slate-500 mb-1.5">עיר מגורים</label><input type="text" name="area" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-blue-500 focus:border-blue-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="למשל: תל אביב"></div>
                    <div><label class="block text-xs font-bold text-slate-500 mb-1.5">כתובת אימייל</label><input type="email" name="email" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-blue-500 focus:border-blue-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="your@email.com"></div>
                    <div class="pt-4 pb-2"><button type="submit" id="subBtn" class="w-full btn-gradient py-4 rounded-xl font-bold text-lg shadow-xl shadow-blue-500/20 flex items-center justify-center gap-2 transform active:scale-[0.98] transition-all">אשר הרשמה<svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg></button><p class="text-center text-[11px] text-slate-400 mt-3">בלחיצה על "אשר הרשמה" הנך מסכים לתנאי השימוש</p></div>
                </form>
            </div>
        </div>
    </div>

    <!-- PERMANENT JOB MODAL -->
    <div id="permanentModal" class="fixed inset-0 bg-slate-900/60 backdrop-blur-sm z-50 hidden flex items-center justify-center p-4">
        <div class="bg-white w-full max-w-2xl rounded-[28px] shadow-2xl flex flex-col max-h-[95vh] overflow-hidden relative">
            <div class="bg-gradient-to-r from-indigo-500 to-purple-600 p-6 pb-4 relative z-10">
                <div class="flex justify-between items-start mb-4">
                    <div><h3 class="text-2xl font-black text-white">הגשת מועמדות</h3><p class="text-indigo-100 text-xs font-bold tracking-wide mt-1">טופס #<span id="random-perm-order-id">4820</span></p></div>
                    <button onclick="closePermanentModal()" class="w-9 h-9 flex items-center justify-center rounded-full bg-white/20 border border-white/30 text-white hover:bg-white/30 transition-colors"><svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg></button>
                </div>
                <div class="bg-white/10 backdrop-blur-sm border border-white/20 rounded-2xl p-4">
                    <div class="text-[10px] font-bold text-indigo-200 uppercase tracking-wider mb-1">המשרה שנבחרה</div>
                    <div id="perm-modal-job-info" class="font-bold text-white text-base leading-tight"></div>
                </div>
            </div>
            <div class="overflow-y-auto p-6 pt-4 scrollbar-hide bg-white flex-1 custom-scrollbar">
                <form id="permRegForm" class="space-y-4">
                    <input type="hidden" name="job_title" id="perm_job_title">
                    <input type="hidden" name="company" id="perm_company">
                    <input type="hidden" name="location" id="perm_location">
                    <div><label class="block text-xs font-bold text-slate-500 mb-1.5">שם מלא</label><input type="text" name="full_name" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-indigo-500 focus:border-indigo-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="שם פרטי ושם משפחה"></div>
                    <div class="grid grid-cols-2 gap-4">
                        <div><label class="block text-xs font-bold text-slate-500 mb-1.5">מספר טלפון</label><input type="tel" id="permPhoneInput" name="phone" required maxlength="10" class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-indigo-500 focus:border-indigo-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="050-0000000"></div>
                        <div><label class="block text-xs font-bold text-slate-500 mb-1.5">תאריך לידה</label><input type="date" name="dob" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-indigo-500 focus:border-indigo-500 block p-3 outline-none transition-all text-slate-500"></div>
                    </div>
                    <div class="grid grid-cols-2 gap-4">
                        <div><label class="block text-xs font-bold text-slate-500 mb-1.5">כתובת אימייל</label><input type="email" name="email" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-indigo-500 focus:border-indigo-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="your@email.com"></div>
                        <div><label class="block text-xs font-bold text-slate-500 mb-1.5">מין</label><select name="gender" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-indigo-500 focus:border-indigo-500 block p-3 outline-none transition-all"><option value="">בחר/י</option><option value="male">זכר</option><option value="female">נקבה</option><option value="other">אחר</option></select></div>
                    </div>
                    <div><label class="block text-xs font-bold text-slate-500 mb-1.5">עיר מגורים</label><input type="text" name="city" required class="w-full bg-slate-50 border border-slate-200 text-slate-800 text-sm rounded-xl focus:ring-indigo-500 focus:border-indigo-500 block p-3 outline-none transition-all placeholder-slate-400" placeholder="למשל: תל אביב"></div>
                    <div class="bg-indigo-50 border border-indigo-100 rounded-xl p-4 flex gap-3 items-start"><div class="bg-indigo-100 p-2 rounded-full text-indigo-600 shrink-0"><svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg></div><div class="text-xs text-indigo-800 leading-relaxed"><strong class="font-bold">שים לב:</strong> המועמדות שלך תועבר למעסיק. תקבל מענה תוך 3-5 ימי עסקים.</div></div>
                    <div class="pt-4 pb-2">
                        <button type="submit" id="permSubBtn" class="w-full btn-permanent py-4 rounded-xl font-bold text-lg shadow-xl shadow-indigo-500/20 flex items-center justify-content gap-2 transform active:scale-[0.98] transition-all relative z-10">
                            <span class="relative z-10">שלח מועמדות</span>
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 relative z-10" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" /></svg>
                        </button>
                        <p class="text-center text-[11px] text-slate-400 mt-3">בלחיצה על "שלח מועמדות" הנך מסכים לתנאי השימוש</p>
                    </div>
                </form>
            </div>
        </div>
    </div>

<script data-cfasync="false" src="/cdn-cgi/scripts/5c5dd728/cloudflare-static/email-decode.min.js"></script><script>
    const CONFIG = {
        TEMP_API_URL: 'https://script.google.com/macros/s/AKfycbxa-fQVDwKy6XwUyyJL36xYbeuThq0skHJwDkUq4o_8lW3V9fG_hABm5yilMFg3bk0/exec',
        TEMP_CSV_URL: 'https://docs.google.com/spreadsheets/d/e/2PACX-1vSdcTCtd8ziXsY3WFwgxpnn6Y215M9DWLM2lKY1JoH9ColD5Cxay4ISl81hPj7IFkSmxu7tojEYWl-j/pub?output=csv',
        PERM_API_URL: 'https://script.google.com/macros/s/AKfycby5fwvIZ4y7TSJfqtX3qw4BfarMlOAwnlANDRhqrdzT1ld_Oh7hmCxfeL9W9Fbuqy2BMA/exec',
        PERM_CSV_URL: 'https://docs.google.com/spreadsheets/d/e/2PACX-1vSpguBUpUkf4hZUWf3g-_NiC1A-EGf5OZ8zpTn1LyXvx2PZnpUsk6VXzindKMfsQWfThpDj-IC6SsT6/pub?output=csv',
        CONTACT_API_URL: 'https://script.google.com/macros/s/AKfycbw_GbmvOnRi5yG_35ObB6eNGmgMBCfEJIs1bS5K_a0goUvIISKcV2QKAn3Be0D5rxUM/exec',
        EMPLOYER_API_URL: 'https://script.google.com/macros/s/AKfycbx4pxvIOHYH4oHybIch2C9gQgOWdRpRZbn0U8DeTD0MZ8VAJhSB8nOCSlls8HCSYpfB/exec',
        TEMP_COLS: { DATE:0, DESC:1, JOB_ID:2, RATE:3, AGE:4, HOURS_START:5, NOTES:6, STATUS:8, COMPANY:10, SECTOR:11, PAY_TYPE:12, ANN_TEXT:13, ANN_STATUS:14, ANN_CATEGORY:15, ANN_DATE:16 },
        PERM_COLS: { TITLE:0, COMPANY:1, LOCATION:2, DESC:3, REQUIREMENTS:4, ADVANTAGES:5, SALARY:6, HOURS:7, BENEFITS:8, CATEGORY:9, NOTES:10, MIN_AGE:11, SECTOR:12, IMAGE_URL:13, STATUS:14 }
    };

    const container = document.getElementById('shifts-container');
    let currentJobType = 'temporary';
    let previousTempCsvData = '';
    let previousPermCsvData = '';
    let allTempShiftsData = [];
    let allPermJobsData = [];
    let isLoading = false;

    let activeAnnouncements = [];
    let currentPanelFilter = 'all';
    let stripFadeIndex = 0;
    let stripFadeTimer = null;

    function categoryToTypes(cat) {
        if (!cat) return [];
        const c = cat.trim().toLowerCase();
        const found = [];
        if (c.includes('דחוף') || c.includes('urgent') || c.includes('חשוב') || c.includes('אזהרה')) found.push('urgent');
        if (c.includes('ביטחוני') || c.includes('אבטחה') || c.includes('security') || c.includes('בטחוני')) found.push('security');
        if (c.includes('חדש')  || c.includes('new')    || c.includes('השקה')  || c.includes('פתיחה')) found.push('new');
        if (c.includes('תחזוקה')|| c.includes('maintenance')|| c.includes('שדרוג')|| c.includes('עדכון')) found.push('warning');
        if (c.includes('הצלחה')|| c.includes('success')|| c.includes('סיום')) found.push('success');
        if (c.includes('כללי') || c.includes('info')) found.push('info');
        return found.length ? found : [];
    }
    function detectAnnType(text) {
        const t = (text || '').toLowerCase();
        if (t.includes('דחוף') || t.includes('חשוב') || t.includes('אזהרה') || t.includes('תקלה') || t.includes('בעיה')) return 'urgent';
        if (t.includes('ביטחוני') || t.includes('אבטחה') || t.includes('בטחוני')) return 'security';
        if (t.includes('חדש') || t.includes('הוספ') || t.includes('פתיחה') || t.includes('זמין') || t.includes('הושק')) return 'new';
        if (t.includes('תחזוקה') || t.includes('שדרוג') || t.includes('עדכון') || t.includes('שינוי')) return 'warning';
        return 'info';
    }

    const ANN_CONFIG = {
        urgent:   { label:'דחוף',    stripClass:'stype-urgent',   cardIcon:'atype-urgent',   typeLabel:'text-red-600',    typeBg:'bg-red-50 text-red-600',    iconSvg:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>',catIcon:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>'},
        security: { label:'ביטחוני',stripClass:'stype-security', cardIcon:'atype-security', typeLabel:'text-green-700',  typeBg:'bg-green-50 text-green-700',iconSvg:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/>',catIcon:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/>'},
        new:      { label:'חדש',     stripClass:'stype-new',      cardIcon:'atype-new',      typeLabel:'text-violet-600', typeBg:'bg-violet-50 text-violet-600',iconSvg:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z"/>',catIcon:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z"/>'},
        warning:  { label:'תחזוקה', stripClass:'stype-warning',  cardIcon:'atype-warning',  typeLabel:'text-amber-600',  typeBg:'bg-amber-50 text-amber-600', iconSvg:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>',catIcon:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"/>'},
        success:  { label:'הצלחה',  stripClass:'stype-success',  cardIcon:'atype-success',  typeLabel:'text-green-600',  typeBg:'bg-green-50 text-green-600', iconSvg:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>',catIcon:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>'},
        info:     { label:'כללי',   stripClass:'stype-info',     cardIcon:'atype-info',     typeLabel:'text-blue-600',   typeBg:'bg-blue-50 text-blue-600',   iconSvg:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>',catIcon:'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>'}
    };

    function stripFadeGoTo(idx) {
        const items = document.querySelectorAll('.strip-fade-item');
        const dots  = document.querySelectorAll('.strip-dot');
        items.forEach((el,i) => el.classList.toggle('active', i===idx));
        dots.forEach((d,i) => d.classList.toggle('active', i===idx));
        stripFadeIndex = idx;
        const strip = document.getElementById('announcementStrip');
        if (strip && activeAnnouncements[idx]) {
            const type = activeAnnouncements[idx].type;
            const cfg  = ANN_CONFIG[type];
            if (cfg) {
                Object.values(ANN_CONFIG).forEach(c => strip.classList.remove(c.stripClass));
                strip.classList.add(cfg.stripClass);
                document.getElementById('stripTypeText').textContent = cfg.label;
            }
        }
    }
    function stripFadeNext() { const items = document.querySelectorAll('.strip-fade-item'); if (!items.length) return; stripFadeGoTo((stripFadeIndex+1)%items.length); }
    function startStripFadeTimer() { if (stripFadeTimer) clearInterval(stripFadeTimer); stripFadeTimer = setInterval(stripFadeNext, 7000); }

    function processAnnouncements(rows) {
        const found = [];
        rows.forEach(row => {
            const text   = (row[CONFIG.TEMP_COLS.ANN_TEXT]     || '').trim();
            const status = (row[CONFIG.TEMP_COLS.ANN_STATUS]   || '').trim();
            const catRaw = (row[CONFIG.TEMP_COLS.ANN_CATEGORY] || '').trim();
            const annDate= (row[CONFIG.TEMP_COLS.ANN_DATE]     || '').trim();
            if (!text) return;
            if (!/מחק|נמחק|בטל/i.test(status)) {
                if (catRaw) {
                    const types = categoryToTypes(catRaw);
                    if (types.length>=2) { types.forEach(type => found.push({text,type,catLabel:ANN_CONFIG[type].label,annDate})); }
                    else { const type = types.length ? types[0] : detectAnnType(text); found.push({text,type,catLabel:ANN_CONFIG[type]?.label||catRaw,annDate}); }
                } else { const type = detectAnnType(text); found.push({text,type,catLabel:ANN_CONFIG[type].label,annDate}); }
            }
        });
        activeAnnouncements = found;
        const strip = document.getElementById('announcementStrip');
        const btn   = document.getElementById('announcementBtn');
        const badge = document.getElementById('announcementBadge');
        if (found.length===0) { strip.classList.remove('visible'); btn.classList.remove('visible','has-new'); badge.classList.remove('visible'); if(stripFadeTimer)clearInterval(stripFadeTimer); return; }
        const dominant = found[found.length-1].type;
        const domCfg   = ANN_CONFIG[dominant];
        strip.className = '';
        strip.classList.add('visible', domCfg.stripClass);
        const stripSingle = document.getElementById('stripSingle');
        const stripMulti  = document.getElementById('stripMulti');
        if (found.length===1) {
            strip.classList.add('single-item'); stripSingle.style.display='flex'; stripMulti.style.display='none';
            if(stripFadeTimer)clearInterval(stripFadeTimer);
            const ann=found[0]; const cfg=ANN_CONFIG[ann.type];
            document.getElementById('stripSingleIconSvg').innerHTML=cfg.catIcon;
            document.getElementById('stripSingleLabel').textContent=ann.catLabel||cfg.label;
            document.getElementById('stripSingleText').textContent=ann.text;
            document.getElementById('stripSingleDate').textContent=ann.annDate||'';
        } else {
            strip.classList.add('multi-item'); stripSingle.style.display='none'; stripMulti.style.display='flex';
            document.getElementById('stripTypeText').textContent=domCfg.label;
            document.getElementById('stripCountBadge').textContent=found.length;
            const wrap=document.getElementById('stripFadeWrap');
            wrap.innerHTML=found.map((ann,i)=>{const cfg=ANN_CONFIG[ann.type];return`<div class="strip-fade-item${i===0?' active':''}">${ann.annDate?`<span class="strip-fade-cat"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor" width="9" height="9">${cfg.catIcon}</svg>${escapeHtml(ann.catLabel)}</span>`:`<span class="strip-fade-cat"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor" width="9" height="9">${cfg.catIcon}</svg>${escapeHtml(ann.catLabel)}</span>`}<span class="strip-fade-text">${escapeHtml(ann.text)}</span>${ann.annDate?`<span class="strip-fade-date">${escapeHtml(ann.annDate)}</span>`:''}</div>`;}).join('');
            const dotsEl=document.getElementById('stripDots');
            dotsEl.innerHTML=found.map((_,i)=>`<button class="strip-dot${i===0?' active':''}" onclick="stripFadeGoTo(${i});startStripFadeTimer()"></button>`).join('');
            stripFadeIndex=0; startStripFadeTimer();
        }
        strip.classList.add('visible'); btn.classList.add('visible','has-new');
        badge.textContent=found.length; badge.classList.add('visible');
        document.getElementById('panelCount').textContent=found.length;
        renderPanelList();
    }

    function renderPanelList() {
        const list=document.getElementById('panelList');
        const filtered=currentPanelFilter==='all'?activeAnnouncements:activeAnnouncements.filter(a=>a.type===currentPanelFilter);
        if(filtered.length===0){list.innerHTML=`<div class="panel-empty"><div class="panel-empty-icon"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg></div><h4>אין עדכונים בקטגוריה זו</h4><p>נסה לבחור קטגוריה אחרת</p></div>`;return;}
        list.innerHTML=filtered.map((ann,i)=>{
            const cfg=ANN_CONFIG[ann.type];
            const dateDisplay=ann.annDate?`<div class="ann-date-display"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/></svg>${escapeHtml(ann.annDate)}</div>`:`<div class="ann-date-display"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"/></svg>עדכון פעיל</div>`;
            return`<div class="ann-card" style="animation:slideUpFade 0.4s ${i*60}ms both"><div class="ann-card-top"><div class="ann-type-icon ${cfg.cardIcon}"><svg fill="none" viewBox="0 0 24 24" stroke="currentColor">${cfg.iconSvg}</svg></div><div class="ann-body"><div class="ann-type-label ${cfg.typeLabel}">${escapeHtml(ann.catLabel||cfg.label)}</div><div class="ann-text">${escapeHtml(ann.text)}</div></div></div><div class="ann-card-bottom">${dateDisplay}<span class="ann-type-badge ${cfg.typeBg}">${escapeHtml(ann.catLabel||cfg.label)}</span></div></div>`;
        }).join('');
    }

    function filterPanel(btn,filter){currentPanelFilter=filter;document.querySelectorAll('.panel-tab').forEach(t=>t.classList.remove('active'));btn.classList.add('active');renderPanelList();}
    function scrollTabs(delta){const tabs=document.getElementById('panelTabs');tabs.scrollBy({left:delta,behavior:'smooth'});setTimeout(updateTabsArrows,150);}
    function updateTabsArrows(){const tabs=document.getElementById('panelTabs');const wrapper=document.getElementById('panelTabsWrapper');if(!tabs||!wrapper)return;wrapper.classList.toggle('can-scroll-right',tabs.scrollLeft>4);wrapper.classList.toggle('can-scroll-left',tabs.scrollLeft<(tabs.scrollWidth-tabs.clientWidth-4));}
    function openPanel(){document.getElementById('announcementPanel').classList.add('open');document.getElementById('panelBackdrop').classList.add('visible');document.body.style.overflow='hidden';renderPanelList();setTimeout(updateTabsArrows,50);}
    function closePanel(){document.getElementById('announcementPanel').classList.remove('open');document.getElementById('panelBackdrop').classList.remove('visible');document.body.style.overflow='';}

    function selectSubject(el){document.querySelectorAll('.contact-subject-chip').forEach(c=>c.classList.remove('selected'));el.classList.add('selected');document.getElementById('contactSubjectInput').value=el.dataset.value;}
    function openContact(){document.getElementById('contactModal').classList.remove('hidden');document.body.style.overflow='hidden';}
    function closeContact(){document.getElementById('contactModal').classList.add('hidden');document.body.style.overflow='';document.getElementById('contactForm').reset();document.querySelectorAll('.contact-subject-chip').forEach(c=>c.classList.remove('selected'));document.getElementById('contactSubjectInput').value='';}
    document.getElementById('contactForm').addEventListener('submit',function(e){
        e.preventDefault();const subject=document.getElementById('contactSubjectInput').value;
        if(!subject){Swal.fire({icon:'warning',title:'נא לבחור נושא',text:'אנא בחר/י את נושא הפנייה',confirmButtonColor:'#3b82f6'});return;}
        const btn=document.getElementById('contactSubmitBtn');const orig=btn.innerHTML;
        btn.disabled=true;btn.innerHTML=`<svg class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg> שולח...`;
        fetch(CONFIG.CONTACT_API_URL,{method:'POST',body:new FormData(this)}).then(r=>r.json()).then(data=>{closeContact();if(data.result==='success'){Swal.fire({title:'ההודעה נשלחה!',text:'נחזור אליך בהקדם האפשרי.',icon:'success',confirmButtonText:'תודה',confirmButtonColor:'#3b82f6'});}else{throw new Error();}}).catch(()=>{Swal.fire({title:'אופס...',text:'הייתה בעיה. ניתן לפנות ישירות ל-jobscohen1@gmail.com',icon:'error',confirmButtonText:'סגור',confirmButtonColor:'#ef4444'});}).finally(()=>{btn.disabled=false;btn.innerHTML=orig;});
    });

    function switchJobType(type){
        currentJobType=type;
        document.getElementById('tempToggle').classList.toggle('active',type==='temporary');
        document.getElementById('permToggle').classList.toggle('active',type==='permanent');
        const ts=document.getElementById('jobTypeTitle'),sub=document.getElementById('jobTypeSubtitle');
        if(type==='temporary'){ts.innerHTML='<span class="text-transparent bg-clip-text bg-gradient-to-r from-blue-600 to-indigo-600">משמרות</span>';sub.textContent='מצאו את המשמרת הבאה שלכם, הירשמו בקליק והתחילו לעבוד.';}
        else{ts.innerHTML='<span class="text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-purple-600">משרות קבועות</span>';sub.textContent='חפשו את המשרה המושלמת שלכם והצטרפו לחברות מובילות.';}
        clearFilters();container.innerHTML='';renderSkeletons();
        if(type==='temporary')loadTempShifts();else loadPermanentJobs();
    }

    const searchInput=document.getElementById('searchInput');
    const tempPlaceholders=["עומר אדם","אבטחה","טדי ירושלים","סדרנות","תל אביב","שכר גבוה","מלצרות"];
    const permPlaceholders=["מנהל/ת מכירות","תל אביב","משרה מלאה","הייטק","שיווק דיגיטלי","שירות לקוחות"];
    let txtIndex=0,charIndex=0,isDeleting=false;
    function typeWriter(){const pls=currentJobType==='temporary'?tempPlaceholders:permPlaceholders;const cur=txtIndex%pls.length;const full=pls[cur];let ph=isDeleting?full.substring(0,charIndex-1):full.substring(0,charIndex+1);searchInput.setAttribute('placeholder',''+ph);if(!isDeleting&&charIndex===full.length){isDeleting=true;setTimeout(typeWriter,2000);}else if(isDeleting&&charIndex===0){isDeleting=false;txtIndex++;setTimeout(typeWriter,500);}else{charIndex=isDeleting?charIndex-1:charIndex+1;setTimeout(typeWriter,isDeleting?50:100);}}
    document.addEventListener('DOMContentLoaded',typeWriter);

    function escapeHtml(t){if(!t)return'';const m={'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'};return t.toString().replace(/[&<>"']/g,c=>m[c]);}
    function renderSkeletons(){
        isLoading=true;let h='';const isT=currentJobType==='temporary';
        for(let i=0;i<6;i++){
            if(isT)h+=`<div class="ticket-card border-none"><div class="p-6 pb-4"><div class="flex justify-between items-start mb-3"><div class="h-7 w-32 skeleton rounded-lg"></div><div class="h-7 w-16 skeleton rounded-md"></div></div><div class="h-6 w-3/4 skeleton rounded mb-2"></div><div class="h-6 w-1/2 skeleton rounded mb-3"></div><div class="flex gap-2"><div class="h-7 w-24 skeleton rounded-lg"></div><div class="h-7 w-28 skeleton rounded-lg"></div></div></div><div class="mt-auto"><div class="ticket-separator"></div><div class="p-5 bg-slate-50/80 flex items-center justify-between gap-3"><div class="h-10 w-20 skeleton rounded"></div><div class="h-11 w-24 skeleton rounded-xl"></div></div></div></div>`;
            else h+=`<div class="re-card"><div class="re-card-hero skeleton" style="border-radius:18px;margin:10px 10px 0;height:200px;"></div><div style="padding:16px 18px 0"><div class="h-3 w-24 skeleton rounded mb-2"></div><div class="h-3 w-32 skeleton rounded mb-2"></div><div class="h-7 w-40 skeleton rounded mb-3"></div><div class="flex gap-2"><div class="h-10 skeleton rounded-xl flex-1"></div><div class="h-10 skeleton rounded-xl flex-1"></div><div class="h-10 skeleton rounded-xl flex-1"></div></div></div><div style="padding:16px 18px 18px"><div class="h-12 skeleton rounded-xl w-full"></div></div></div>`;
        }
        container.innerHTML=h;
    }
    function isValidIsraeliID(id){let s=String(id).trim();if(s.length>9||s.length<5)return false;s=s.padStart(9,'0');if(s==='000000000')return false;let sum=0;for(let i=0;i<9;i++){let n=Number(s.charAt(i));let st=n*((i%2)+1);if(st>9)st-=9;sum+=st;}return sum%10===0;}
    function isValidPhone(p){return /^05\d{8}$/.test(p.replace(/[-\s]/g,''));}
    function cleanStr(s){if(!s)return'';return s.replace(/['"]/g,'').replace(/(\r\n|\n|\r)/gm," ").trim();}
    function findHoursInRow(row){for(let i=1;i<row.length;i++){let c=row[i];if(c&&/\d{1,2}:\d{2}/.test(c))return cleanStr(c);}return"משמרת כללית";}
    function getSectorColor(s){const c=['bg-indigo-50 text-indigo-700 border-indigo-100','bg-rose-50 text-rose-700 border-rose-100','bg-emerald-50 text-emerald-700 border-emerald-100','bg-cyan-50 text-cyan-700 border-cyan-100'];if(!s)return'bg-slate-50 text-slate-600 border-slate-200';return c[s.length%c.length];}
    function parseDate(ds){if(!ds)return null;const p=ds.split(/[\/\-\.]/);if(p.length<2)return null;let d=parseInt(p[0],10),m=parseInt(p[1],10)-1,y=new Date().getFullYear();if(p.length>=3){y=parseInt(p[2],10);if(y<100)y+=2000;}return new Date(y,m,d);}
    function toSegments(str){if(!str)return[];let p=str.split(/[;；]/).map(s=>s.trim()).filter(Boolean);if(p.length>=2)return p;p=str.split(',').map(s=>s.trim()).filter(Boolean);if(p.length>=3)return p;p=str.split(/\s*[•\-\*]\s+/).map(s=>s.trim()).filter(Boolean);if(p.length>=2)return p;return[str.trim()];}
    function renderField(raw,lc=''){if(!raw)return'';const s=toSegments(raw);if(s.length>=2)return`<ul class="jdm-list ${lc}">${s.map(x=>`<li>${escapeHtml(x)}</li>`).join('')}</ul>`;return`<p class="jdm-para">${escapeHtml(raw).replace(/\n/g,'<br>')}</p>`;}

    function openJobModal(id){
        const item=allPermJobsData[parseInt(id)];if(!item)return;
        const FB='https://images.unsplash.com/photo-1497366216548-37526070297c?w=800&q=80';
        const iDoc=`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/></svg>`;
        const iCheck=`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>`;
        const iBolt=`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/></svg>`;
        const iGift=`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v13m0-13V6a2 2 0 112 2h-2zm0 0V5.5A2.5 2.5 0 109.5 8H12zm-7 4h14M5 12a2 2 0 110-4h14a2 2 0 110 4M5 12v7a2 2 0 002 2h10a2 2 0 002-2v-7"/></svg>`;
        const iClock=`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>`;
        const iPin=`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/></svg>`;
        const iWarn=`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/></svg>`;
        const iUser=`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>`;
        const chips=[];
        if(item.hours)chips.push({icon:iClock,text:escapeHtml(item.hours)});
        if(item.minAge)chips.push({icon:iUser,text:escapeHtml(item.minAge)+'+'});
        const chipsHtml=chips.map(c=>`<div class="jdm-info-chip">${c.icon}<span>${c.text}</span></div>`).join('');
        function sec(iconEl,iconCls,title,sub,raw,lc=''){if(!raw)return'';return`<div class="jdm-section"><div class="jdm-sec-head"><div class="jdm-sec-icon ${iconCls}">${iconEl}</div><div><div class="jdm-sec-title">${title}</div>${sub?`<div class="jdm-sec-subtitle">${sub}</div>`:''}</div></div><div class="jdm-sec-body">${renderField(raw,lc)}</div></div>`;}
        const eT=escapeHtml(item.title),eCo=escapeHtml(item.company),eLo=escapeHtml(item.location);
        document.getElementById('jdm-content').innerHTML=`
            <div class="jdm-hero">
                <img src="${item.imageUrl||FB}" alt="${eT}" onerror="this.src='${FB}'">
                <div class="jdm-hero-overlay"></div>
                ${item.sector||item.category?`<div class="jdm-hero-type">${escapeHtml(item.sector||item.category)}</div>`:''}
            </div>
            <div class="jdm-info-card">
                ${item.location?`<div class="jdm-info-location">${iPin}${eLo}</div>`:''}
                ${item.company?`<div class="jdm-info-company">${eCo}</div>`:''}
                <div class="jdm-info-price">${item.salary?escapeHtml(item.salary):'שכר לפי ניסיון'}</div>
                ${chipsHtml?`<div class="jdm-info-chips">${chipsHtml}</div>`:''}
            </div>
            <div class="jdm-sections">
                ${sec(iDoc,'si2-indigo','תיאור התפקיד','על המשרה',item.desc)}
                ${sec(iCheck,'si2-blue','דרישות חובה','חובה להגעה לראיון',item.requirements,'req')}
                ${sec(iBolt,'si2-green','יתרון למועמד','לא חובה אך מועיל',item.advantages,'adv')}
                ${sec(iGift,'si2-amber','הטבות ותנאים','מה תקבל/י',item.benefits,'ben')}
                ${item.notes?`<div class="jdm-section"><div class="jdm-note-box">${iWarn}<p>${escapeHtml(item.notes)}</p></div></div>`:''}
            </div>`;
        const ctaEl = document.getElementById('jdm-cta-fixed');
        const ctaBtn = document.getElementById('jdm-cta-btn');
        ctaEl.style.display = 'block';
        ctaBtn.onclick = () => { closeJobModal(); openPermanentModal(item.title, item.company, item.location); };
        const iClockS=`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>`;
        const iPinS=`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/></svg>`;
        const iUserS=`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>`;
        document.getElementById('jdm-left-img').src = item.imageUrl || FB;
        document.getElementById('jdm-left-title').textContent = item.title;
        document.getElementById('jdm-left-company').textContent = item.company;
        document.getElementById('jdm-left-salary').textContent = item.salary || 'שכר לפי ניסיון';
        const typeEl = document.getElementById('jdm-left-type');
        typeEl.textContent = item.sector || item.category || '';
        typeEl.style.display = (item.sector || item.category) ? '' : 'none';
        const locEl = document.getElementById('jdm-left-location');
        locEl.innerHTML = item.location ? `${iPinS}${escapeHtml(item.location)}` : '';
        const leftChips = [];
        if(item.hours) leftChips.push({icon:iClockS, text:item.hours});
        if(item.minAge) leftChips.push({icon:iUserS, text:`גיל ${item.minAge}+`});
        document.getElementById('jdm-left-chips').innerHTML = leftChips.map(c=>`<div class="jdm-left-chip">${c.icon}<span>${escapeHtml(c.text)}</span></div>`).join('');
        document.getElementById('jobDetailsModal').classList.add('active');
        document.body.style.overflow='hidden';
        document.getElementById('jdm-scroll').scrollTop=0;
    }
    function toggleJobModalFullscreen(){const bd=document.getElementById('jobDetailsModal');const icon=document.getElementById('jdmExpandIcon');bd.classList.toggle('fullscreen');const isFs=bd.classList.contains('fullscreen');icon.innerHTML=isFs?'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 9L4 4m0 0h5M4 4v5m11-5l5 5m0-5h-5m0 0v5M4 20l5-5m-5 5h5m0 0v-5m11 5l-5-5m5 5v-4m0 5h-5"/>':'<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8V4m0 0h4M4 4l5 5m11-1V4m0 0h-4m4 0l-5 5M4 16v4m0 0h4m-4 0l5-5m11 5l-5-5m5 5v-4m0 4h-4"/>';}
    function closeJobModal(){document.getElementById('jobDetailsModal').classList.remove('active','fullscreen');document.body.style.overflow='';const ctaEl=document.getElementById('jdm-cta-fixed');if(ctaEl)ctaEl.style.display='none';}

    function loadTempShifts(){
        if(!document.getElementById('modal').classList.contains('hidden'))return;
        const cb='&t='+new Date().getTime();
        fetch(CONFIG.TEMP_CSV_URL+cb,{cache:"no-store",headers:{'Pragma':'no-cache','Cache-Control':'no-cache'}})
            .then(r=>r.text()).then(csv=>{
                if(csv===previousTempCsvData&&!isLoading)return;
                previousTempCsvData=csv;
                Papa.parse(csv,{header:false,skipEmptyLines:true,complete:(results)=>{
                    const rows=results.data.slice(1);
                    processAnnouncements(rows);
                    allTempShiftsData=rows.map(row=>{
                        if(!row[0])return null;
                        if(row[CONFIG.TEMP_COLS.STATUS]){const s=cleanStr(row[CONFIG.TEMP_COLS.STATUS]);if(s.includes('מחק')||s.includes('נמחק')||s.includes('בוטל'))return null;}
                        const ds=cleanStr(row[CONFIG.TEMP_COLS.DATE]);const jd=parseDate(ds);
                        const today=new Date();today.setHours(0,0,0,0);
                        if(jd&&jd<today)return null;
                        return{rawRow:row,dateStr:ds,jsDate:jd,desc:cleanStr(row[CONFIG.TEMP_COLS.DESC])||"עובד כללי",hours:findHoursInRow(row),ageLimit:cleanStr(row[CONFIG.TEMP_COLS.AGE]),rate:parseFloat(row[CONFIG.TEMP_COLS.RATE]||'0'),rateStr:row[CONFIG.TEMP_COLS.RATE]||'0',jobId:row[CONFIG.TEMP_COLS.JOB_ID]||'00',company:cleanStr(row[CONFIG.TEMP_COLS.COMPANY]),sector:cleanStr(row[CONFIG.TEMP_COLS.SECTOR]),payType:cleanStr(row[CONFIG.TEMP_COLS.PAY_TYPE]),notes:row[CONFIG.TEMP_COLS.NOTES]};
                    }).filter(x=>x!==null);
                    isLoading=false;applyFilters();
                }});
            }).catch(err=>{console.error(err);isLoading=false;});
    }

    function loadPermanentJobs(){
        if(!document.getElementById('permanentModal').classList.contains('hidden'))return;
        const cb='&t='+new Date().getTime();
        fetch(CONFIG.PERM_CSV_URL+cb,{cache:"no-store",headers:{'Pragma':'no-cache','Cache-Control':'no-cache'}})
            .then(r=>r.text()).then(csv=>{
                if(csv===previousPermCsvData&&!isLoading)return;
                previousPermCsvData=csv;
                Papa.parse(csv,{header:false,skipEmptyLines:true,complete:(results)=>{
                    const rows=results.data.slice(1);
                    allPermJobsData=rows.map(row=>{
                        if(!row[0])return null;
                        if(row[CONFIG.PERM_COLS.STATUS]){const s=cleanStr(row[CONFIG.PERM_COLS.STATUS]);if(s.includes('מחק')||s.includes('נמחק')||s.includes('סגור'))return null;}
                        return{rawRow:row,title:cleanStr(row[CONFIG.PERM_COLS.TITLE])||"משרה",company:cleanStr(row[CONFIG.PERM_COLS.COMPANY])||"",location:cleanStr(row[CONFIG.PERM_COLS.LOCATION])||"",desc:cleanStr(row[CONFIG.PERM_COLS.DESC])||"",requirements:cleanStr(row[CONFIG.PERM_COLS.REQUIREMENTS])||"",advantages:cleanStr(row[CONFIG.PERM_COLS.ADVANTAGES])||"",salary:cleanStr(row[CONFIG.PERM_COLS.SALARY])||"",hours:cleanStr(row[CONFIG.PERM_COLS.HOURS])||"",benefits:cleanStr(row[CONFIG.PERM_COLS.BENEFITS])||"",category:cleanStr(row[CONFIG.PERM_COLS.CATEGORY])||"",notes:cleanStr(row[CONFIG.PERM_COLS.NOTES])||"",minAge:cleanStr(row[CONFIG.PERM_COLS.MIN_AGE])||"",sector:cleanStr(row[CONFIG.PERM_COLS.SECTOR])||"",imageUrl:cleanStr(row[CONFIG.PERM_COLS.IMAGE_URL])||""};
                    }).filter(x=>x!==null);
                    isLoading=false;applyFilters();
                }});
            }).catch(err=>{console.error(err);isLoading=false;});
    }

    function applyFilters(){
        const sv=document.getElementById('searchInput').value.toLowerCase();
        const lv=document.getElementById('locationFilter').value.toLowerCase();
        const rv=parseFloat(document.getElementById('rateFilter').value)||0;
        const av=parseInt(document.getElementById('ageFilter').value)||0;
        if(currentJobType==='temporary'){
            let f=allTempShiftsData.filter(item=>{
                const tm=item.desc.toLowerCase().includes(sv)||item.company.toLowerCase().includes(sv)||item.dateStr.includes(sv);
                const lm=!lv||item.desc.toLowerCase().includes(lv)||item.company.toLowerCase().includes(lv);
                const rm=item.rate>=rv;let am=true;
                if(av>0&&item.ageLimit){const sa=parseInt(item.ageLimit.replace(/\D/g,''))||0;if(sa>av)am=false;}
                return tm&&lm&&rm&&am;
            });
            f.sort((a,b)=>{if(!a.jsDate)return 1;if(!b.jsDate)return -1;return a.jsDate-b.jsDate;});
            document.getElementById('resultsCount').innerText=`נמצאו ${f.length} משמרות`;
            renderTempShiftsHTML(f);
        }else{
            let f=allPermJobsData.filter(item=>{
                const tm=item.title.toLowerCase().includes(sv)||item.company.toLowerCase().includes(sv)||item.category.toLowerCase().includes(sv)||item.desc.toLowerCase().includes(sv);
                const lm=!lv||item.location.toLowerCase().includes(lv);
                let am=true;if(av>0&&item.minAge){const ja=parseInt(item.minAge.replace(/\D/g,''))||0;if(ja>av)am=false;}
                return tm&&lm&&am;
            });
            document.getElementById('resultsCount').innerText=`נמצאו ${f.length} משרות`;
            renderPermJobsHTML(f);
        }
    }
    function clearFilters(){document.getElementById('searchInput').value='';document.getElementById('locationFilter').value='';document.getElementById('rateFilter').value='';document.getElementById('ageFilter').value='';applyFilters();}

    function renderTempShiftsHTML(shifts){
        let html='';let delay=0;
        shifts.forEach(item=>{
            const safeHours=escapeHtml(item.hours);const rawDate=escapeHtml(item.dateStr);const descPart=escapeHtml(item.desc);const company=escapeHtml(item.company);const sectorTagClass=getSectorColor(item.sector);
            let payTypeLabel='תעריף שעתי';if(item.payType&&(item.payType.includes('יומי')||item.payType.includes('גלובלי')||item.payType.includes('פרויקט')))payTypeLabel='שכר יומי';
            const notes=item.notes?`<div class="mt-4 text-xs text-amber-700 bg-amber-50 p-3 rounded-lg border border-amber-100 flex items-start gap-2 leading-relaxed"><svg class="w-4 h-4 shrink-0 mt-0.5 text-amber-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>${escapeHtml(item.notes)}</div>`:'';
            const ageBadge=item.ageLimit?`<div class="flex items-center gap-1 bg-slate-800 text-white text-[10px] font-bold px-2 py-1 rounded-md shrink-0 shadow-sm"><svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>${item.ageLimit}</div>`:'';
            const safeDetails=`${rawDate} | ${descPart}`;
            const shareId=`${item.jobId||'job'}_${item.dateStr||''}`.replace(/\s+/g,'_').replace(/[^\w\-]/g,'');
            delay+=30;
            html+=`<div class="ticket-card animate-card group" style="animation-delay:${delay}ms"><div class="p-6 pb-4 flex-grow"><div class="flex justify-between items-start mb-3"><div class="flex items-center gap-2 text-slate-500 text-sm font-bold bg-slate-50 px-2 py-1 rounded-lg border border-slate-100"><svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>${rawDate}</div>${ageBadge}</div><h3 class="text-xl font-black text-slate-800 leading-snug mb-3 line-clamp-2 min-h-[3.5rem] group-hover:text-blue-600 transition-colors">${descPart}</h3><div class="flex flex-wrap gap-2 mb-2">${item.sector?`<span class="px-2.5 py-1 rounded-lg text-xs font-bold border ${sectorTagClass}">${item.sector}</span>`:''}<span class="px-2.5 py-1 rounded-lg text-xs font-bold bg-white border border-slate-200 text-slate-500 flex items-center gap-1 shadow-sm"><svg class="w-3 h-3 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>${safeHours}</span></div>${notes}</div><div class="mt-auto"><div class="ticket-separator"></div><div class="p-5 bg-slate-50/80 flex items-center justify-between gap-3"><div><div class="text-3xl font-black text-transparent bg-clip-text bg-gradient-to-l from-slate-900 to-slate-700 leading-none">₪${item.rateStr}</div><div class="text-[11px] font-bold text-slate-400 mt-1 uppercase tracking-wide">${payTypeLabel}</div></div><div class="flex items-center gap-2"><button onclick="openSharePopup('${escapeHtml(item.desc)}','${escapeHtml(item.company)}','${shareId}')" title="שתף משמרת" style="width:38px;height:38px;border-radius:10px;background:#f1f5f9;border:1.5px solid #e2e8f0;display:flex;align-items:center;justify-content:center;cursor:pointer;transition:all 0.18s;flex-shrink:0;" onmouseover="this.style.background='#dbeafe';this.style.borderColor='#93c5fd'" onmouseout="this.style.background='#f1f5f9';this.style.borderColor='#e2e8f0'"><svg width="15" height="15" fill="none" viewBox="0 0 24 24" stroke="#3b82f6" stroke-width="2.2"><path stroke-linecap="round" stroke-linejoin="round" d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z"/></svg></button><button onclick="openModal('${safeDetails}','${safeHours}','${rawDate}','${company}')" class="btn-gradient px-7 py-3 rounded-xl font-bold text-sm shadow-md flex items-center gap-2 group/btn">הירשם<svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 group-hover/btn:-translate-x-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" /></svg></button></div></div></div></div>`;
        });
        if(shifts.length===0)html=`<div class="col-span-full flex flex-col items-center justify-center py-24 text-center"><div class="bg-white p-6 rounded-full shadow-soft mb-6 animate-bounce"><svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-slate-300" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg></div><h3 class="text-2xl font-black text-slate-700">לא נמצאו משמרות</h3><p class="text-slate-500 mt-2">נסה לשנות את הסינון.</p><button onclick="clearFilters()" class="mt-4 text-blue-600 font-bold hover:underline">נקה סינון</button></div>`;
        container.innerHTML=html;
    }

    function renderPermJobsHTML(jobs) {
        const FB = 'https://images.unsplash.com/photo-1497366216548-37526070297c?w=800&q=80';
        const iPin    = `<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/></svg>`;
        const iClock  = `<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>`;
        const iWork   = `<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/></svg>`;
        const iUser   = `<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>`;
        const iExpand = `<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8V4m0 0h4M4 4l5 5m11-1V4m0 0h-4m4 0l-5 5M4 16v4m0 0h4m-4 0l5-5m11 5l-5-5m5 5v-4m0 4h-4"/></svg>`;
        const iHeart  = `<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/></svg>`;
        const iArr    = `<svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M10 19l-7-7m0 0l7-7m-7 7h18"/></svg>`;

        let html = '';
        let delay = 0;

        if (!jobs.length) {
            // Show empty state + CTA card
            html = `<div class="col-span-full flex flex-col items-center justify-center py-24 text-center"><div class="bg-white p-6 rounded-full shadow-soft mb-6 animate-bounce"><svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-slate-300" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" /></svg></div><h3 class="text-2xl font-black text-slate-700">לא נמצאו משרות</h3><p class="text-slate-500 mt-2">נסה לשנות את הסינון.</p><button onclick="clearFilters()" class="mt-4 text-indigo-600 font-bold hover:underline">נקה סינון</button></div>`;
            container.innerHTML = html;
            return;
        }

        jobs.forEach((item, i) => {
            const img = item.imageUrl || FB;
            const eT  = escapeHtml(item.title);
            const eCo = escapeHtml(item.company);
            const eLo = escapeHtml(item.location);
            delay += 45;
            const chips = [];
            if (item.hours)  chips.push({ icon: iClock, text: escapeHtml(item.hours) });
            if (item.sector || item.category) chips.push({ icon: iWork, text: escapeHtml(item.sector || item.category) });
            if (item.minAge) chips.push({ icon: iUser, text: `${escapeHtml(item.minAge)}+` });
            const chipsHtml = chips.map(c=>`<div class="re-card-chip">${c.icon}<span>${c.text}</span></div>`).join('');
            const priceDisplay = item.salary
                ? `<div class="re-card-price">${escapeHtml(item.salary)}</div>`
                : `<div class="re-card-price" style="font-size:18px;color:#64748b">שכר לפי ניסיון</div>`;
            html += `
            <div class="re-card animate-card" style="animation-delay:${delay}ms">
                <div class="re-card-hero">
                    <img src="${img}" alt="${eT}" loading="lazy" onerror="this.src='${FB}'">
                    <div class="re-card-hero-overlay"></div>
                    ${item.sector||item.category?`<div class="re-card-type-badge">${escapeHtml(item.sector||item.category)}</div>`:''}
                    <button class="re-card-fav" onclick="this.classList.toggle('active')" title="שמור מועדפים">${iHeart}</button>
                </div>
                <div class="re-card-body">
                    ${item.location?`<div class="re-card-address">${iPin}${eLo}</div>`:''}
                    <div class="re-card-title">${eCo||eT}</div>
                    ${priceDisplay}
                    ${chips.length?`<div class="re-card-chips">${chipsHtml}</div>`:''}
                </div>
                <div class="re-card-footer">
                    <div class="re-card-footer-row">
                        <div class="re-book-btn-wrap">
                            <button class="re-book-btn" onclick="openPermanentModal('${eT}','${eCo}','${eLo}')">
                                הגש מועמדות ${iArr}
                            </button>
                        </div>
                        <button class="re-details-btn" onclick="openSharePopup('${eT}','${eCo}','perm_${i}')" title="שתף משרה" style="margin-left:4px">${`<svg fill="none" viewBox="0 0 24 24" stroke="currentColor" width="16" height="16"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.2" d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z"/></svg>`}</button>
                        <button class="re-details-btn" onclick="openJobModal('${i}')" title="פרטים נוספים">${iExpand}</button>
                    </div>
                </div>
            </div>`;
        });

        // ══ CTA CARD - "Want your job listed here?" ══
        delay += 60;
        html += `
        <div class="re-card re-card-cta animate-card" style="animation-delay:${delay}ms">
            <div class="cta-hero-bg">
                <div class="cta-hero-circle" style="width:180px;height:180px;top:-40px;right:-40px;"></div>
                <div class="cta-hero-circle" style="width:100px;height:100px;bottom:-20px;left:-20px;"></div>
                <div class="cta-hero-circle" style="width:60px;height:60px;top:20px;left:30px;opacity:0.15;"></div>
                <div style="position:relative;z-index:1;text-align:center;padding:20px;">
                    <div style="width:68px;height:68px;background:rgba(255,255,255,0.18);border:2px solid rgba(255,255,255,0.35);border-radius:50%;display:flex;align-items:center;justify-content:center;margin:0 auto 14px;">
                        <svg width="30" height="30" fill="none" viewBox="0 0 24 24" stroke="white" stroke-width="1.7">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"/>
                        </svg>
                    </div>
                    <div style="font-size:16px;font-weight:900;color:white;line-height:1.35;text-shadow:0 1px 6px rgba(0,0,0,0.25);">רוצים שהמשרה<br>שלכם תופיע כאן?</div>
                </div>
            </div>
            <div class="re-card-body" style="padding:14px 16px 0;">
                <div class="re-card-address" style="color:#6366f1;font-weight:800;font-size:11.5px;">
                    <svg width="11" height="11" fill="none" viewBox="0 0 24 24" stroke="currentColor" style="flex-shrink:0"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"/></svg>
                    פרסמו אצלנו
                </div>
                <div class="re-card-title" style="color:#4f46e5;font-size:13px;">הגיעו לאלפי מועמדים מתאימים</div>
                <div style="font-size:20px;font-weight:900;color:#6366f1;line-height:1.15;letter-spacing:-0.3px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">פרסום משרות קבועות</div>
            </div>
            <div class="re-card-footer">
                <div class="re-card-footer-row">
                    <div class="re-book-btn-wrap">
                        <button class="cta-book-btn" onclick="openEmployerModal()">
                            <svg width="15" height="15" fill="none" viewBox="0 0 24 24" stroke="currentColor" style="flex-shrink:0"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/></svg>
                            שלחו פרטים
                        </button>
                    </div>
                </div>
            </div>
        </div>`;

        container.innerHTML = html;
    }

    renderSkeletons();
    loadTempShifts();
    document.addEventListener('DOMContentLoaded', () => {
        const tabs = document.getElementById('panelTabs');
        if (tabs) tabs.addEventListener('scroll', updateTabsArrows, { passive: true });
    });
    setInterval(()=>{ loadTempShifts(); if(currentJobType==='permanent')loadPermanentJobs(); }, 30000);

    // ══ DEEP LINK — ?job=ID ══
    // נקרא אחרי שהנתונים נטענו, מחפש את המשרה ופותח פופאפ
    function handleDeepLink() {
        const params = new URLSearchParams(window.location.search);
        const jobId  = params.get('job');
        if (!jobId) return;

        // נסה למצוא במשמרות זמניות
        const tempItem = allTempShiftsData.find(item => {
            if (!item) return false;
            const id = (item.jobId || 'job') + '_' + (item.dateStr || '');
            return id.replace(/\s+/g,'_').replace(/[^\w\-]/g,'') === jobId;
        });
        if (tempItem) {
            // החלף לטאב משמרות
            if (currentJobType !== 'temporary') switchJobType('temporary');
            setTimeout(() => {
                Swal.fire({
                    title: tempItem.desc,
                    html: `
                        <div style="text-align:right;font-family:Heebo,sans-serif;">
                            <div style="font-size:13px;color:#64748b;margin-bottom:10px;">${escapeHtml(tempItem.dateStr)} | ${escapeHtml(tempItem.hours)}</div>
                            <div style="font-size:28px;font-weight:900;color:#0f172a;margin-bottom:6px;">₪${escapeHtml(tempItem.rateStr)}</div>
                            ${tempItem.sector ? `<div style="font-size:12px;font-weight:700;color:#3b82f6;margin-bottom:8px;">${escapeHtml(tempItem.sector)}</div>` : ''}
                            ${tempItem.notes ? `<div style="font-size:12px;color:#b45309;background:#fffbeb;padding:8px 12px;border-radius:10px;margin-top:8px;">${escapeHtml(tempItem.notes)}</div>` : ''}
                        </div>`,
                    confirmButtonText: 'הירשם למשמרת',
                    showCancelButton: true,
                    cancelButtonText: 'סגור',
                    confirmButtonColor: '#1e293b',
                    cancelButtonColor: '#94a3b8',
                    customClass: { popup: 'swal2-rtl' }
                }).then(res => {
                    if (res.isConfirmed) {
                        const safeDetails = `${escapeHtml(tempItem.dateStr)} | ${escapeHtml(tempItem.desc)}`;
                        openModal(safeDetails, escapeHtml(tempItem.hours), escapeHtml(tempItem.dateStr), escapeHtml(tempItem.company));
                    }
                });
            }, 600);
            return;
        }

        // נסה למצוא במשרות קבועות (perm_INDEX)
        const permMatch = jobId.match(/^perm_(\d+)$/);
        if (permMatch) {
            const idx = parseInt(permMatch[1]);
            if (currentJobType !== 'permanent') {
                switchJobType('permanent');
                // המתן לטעינה ואז פתח
                const waitForPerm = setInterval(() => {
                    if (allPermJobsData.length > 0) {
                        clearInterval(waitForPerm);
                        setTimeout(() => openJobModal(idx), 400);
                    }
                }, 200);
            } else {
                setTimeout(() => openJobModal(idx), 400);
            }
            return;
        }

        // לא נמצא — הצג הודעה עדינה
        setTimeout(() => {
            Swal.fire({
                icon: 'info',
                title: 'המשרה לא נמצאה',
                text: 'ייתכן שהמשרה כבר לא פעילה. בדוק את המשרות הזמינות.',
                confirmButtonColor: '#3b82f6',
                confirmButtonText: 'לכל המשרות'
            });
        }, 800);
    }

    // הרץ deep link אחרי שהנתונים נטענו
    const _origLoadTemp = loadTempShifts;
    let _deepLinkHandled = false;
    loadTempShifts = function() {
        _origLoadTemp.apply(this, arguments);
    };
    // hook into applyFilters לזיהוי שהנתונים מוכנים
    const _deepLinkCheck = setInterval(() => {
        if (allTempShiftsData.length > 0 || allPermJobsData.length > 0) {
            clearInterval(_deepLinkCheck);
            if (!_deepLinkHandled) {
                _deepLinkHandled = true;
                handleDeepLink();
            }
        }
    }, 300);

    function openModal(details,hours,date,company){document.getElementById('shift_details').value=details;document.getElementById('shift_hours_input').value=hours;document.getElementById('shift_date_input').value=date;document.getElementById('company_input').value=company;document.getElementById('modal-shift-info').innerText=details;document.getElementById('random-order-id').innerText=Math.floor(1000+Math.random()*9000);document.getElementById('modal').classList.remove('hidden');document.body.style.overflow='hidden';}
    function closeModal(){document.getElementById('modal').classList.add('hidden');document.body.style.overflow='';document.getElementById('regForm').reset();}
    function openPermanentModal(title,company,location){document.getElementById('perm_job_title').value=title;document.getElementById('perm_company').value=company;document.getElementById('perm_location').value=location;document.getElementById('perm-modal-job-info').innerText=`${title} - ${company} (${location})`;document.getElementById('random-perm-order-id').innerText=Math.floor(1000+Math.random()*9000);document.getElementById('permanentModal').classList.remove('hidden');document.body.style.overflow='hidden';}
    function closePermanentModal(){document.getElementById('permanentModal').classList.add('hidden');document.body.style.overflow='';document.getElementById('permRegForm').reset();}
    document.getElementById('modal').addEventListener('click',e=>{if(e.target===document.getElementById('modal'))closeModal();});
    document.getElementById('permanentModal').addEventListener('click',e=>{if(e.target===document.getElementById('permanentModal'))closePermanentModal();});
    function openTerms(){document.getElementById('termsModal').classList.remove('hidden');document.body.style.overflow='hidden';}
    function closeTerms(){document.getElementById('termsModal').classList.add('hidden');document.body.style.overflow='';}
    document.addEventListener('keydown',e=>{if(e.key==="Escape"){closeTerms();closeContact();closeModal();closePermanentModal();closeJobModal();closePanel();}});
    function toggleFilters(){const f=document.getElementById('advancedFilters');if(f.classList.contains('hidden')){f.classList.remove('hidden');f.classList.add('grid');}else{f.classList.add('hidden');f.classList.remove('grid');}}

    document.getElementById('regForm').addEventListener('submit',function(e){
        e.preventDefault();const btn=document.getElementById('subBtn');const orig=btn.innerHTML;
        const idVal=document.getElementById('idInput').value;const phoneVal=document.getElementById('phoneInput').value;
        if(!isValidIsraeliID(idVal)){Swal.fire({icon:'error',title:'תעודת זהות לא תקינה',text:'נא לבדוק את המספר שהוזן',confirmButtonColor:'#3b82f6'});return;}
        if(!isValidPhone(phoneVal)){Swal.fire({icon:'error',title:'מספר טלפון לא תקין',text:'נא להזין מספר נייד תקין',confirmButtonColor:'#3b82f6'});return;}
        btn.disabled=true;btn.innerHTML=`<svg class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg> שולח...`;
        fetch(CONFIG.TEMP_API_URL,{method:'POST',body:new FormData(this)}).then(r=>r.json()).then(data=>{closeModal();if(data.result==='success'){Swal.fire({title:'נרשמת בהצלחה!',text:'הפרטים שלך נקלטו.',icon:'success',confirmButtonText:'מעולה',confirmButtonColor:'#3b82f6'});}else throw new Error();}).catch(()=>{Swal.fire({title:'אופס...',text:'הייתה בעיה.',icon:'error',confirmButtonText:'סגור',confirmButtonColor:'#ef4444'});}).finally(()=>{btn.disabled=false;btn.innerHTML=orig;});
    });
    document.getElementById('permRegForm').addEventListener('submit',function(e){
        e.preventDefault();const btn=document.getElementById('permSubBtn');const orig=btn.innerHTML;
        const phoneVal=document.getElementById('permPhoneInput').value;
        if(!isValidPhone(phoneVal)){Swal.fire({icon:'error',title:'מספר טלפון לא תקין',text:'נא להזין מספר נייד תקין',confirmButtonColor:'#6366f1'});return;}
        btn.disabled=true;btn.innerHTML=`<svg class="animate-spin h-5 w-5 text-white relative z-10" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>`;
        fetch(CONFIG.PERM_API_URL,{method:'POST',body:new FormData(this)}).then(r=>r.json()).then(data=>{closePermanentModal();if(data.result==='success'){Swal.fire({title:'המועמדות נשלחה!',text:'נחזור אליך בהקדם.',icon:'success',confirmButtonText:'מעולה',confirmButtonColor:'#6366f1'});}else throw new Error();}).catch(()=>{Swal.fire({title:'אופס...',text:'הייתה בעיה.',icon:'error',confirmButtonText:'סגור',confirmButtonColor:'#ef4444'});}).finally(()=>{btn.disabled=false;btn.innerHTML=orig;});
    });
    </script>

    <!-- ══ SHARE POPUP ══ -->
    <div class="share-popup-backdrop" id="sharePopup" onclick="if(event.target===this)closeSharePopup()">
        <div class="share-popup">
            <div class="share-handle"></div>
            <div class="share-header">
                <div class="share-title">שתף משמרת / משרה</div>
                <button class="share-close" onclick="closeSharePopup()">
                    <svg fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12"/></svg>
                </button>
            </div>
            <div class="share-job-info">
                <div class="share-job-info-title" id="shareJobTitle"></div>
                <div class="share-job-info-sub" id="shareJobSub"></div>
            </div>
            <div class="share-btns">
                <button class="share-btn" id="shareWhatsAppBtn">
                    <div class="share-btn-icon" style="background:#dcfce7;">
                        <svg viewBox="0 0 24 24" fill="#16a34a"><path d="M17.472 14.382c-.297-.149-1.758-.867-2.03-.967-.273-.099-.471-.148-.67.15-.197.297-.767.966-.94 1.164-.173.199-.347.223-.644.075-.297-.15-1.255-.463-2.39-1.475-.883-.788-1.48-1.761-1.653-2.059-.173-.297-.018-.458.13-.606.134-.133.298-.347.446-.52.149-.174.198-.298.298-.497.099-.198.05-.371-.025-.52-.075-.149-.669-1.612-.916-2.207-.242-.579-.487-.5-.669-.51-.173-.008-.371-.01-.57-.01-.198 0-.52.074-.792.372-.272.297-1.04 1.016-1.04 2.479 0 1.462 1.065 2.875 1.213 3.074.149.198 2.096 3.2 5.077 4.487.709.306 1.262.489 1.694.625.712.227 1.36.195 1.871.118.571-.085 1.758-.719 2.006-1.413.248-.694.248-1.289.173-1.413-.074-.124-.272-.198-.57-.347m-5.421 7.403h-.004a9.87 9.87 0 01-5.031-1.378l-.361-.214-3.741.982.998-3.648-.235-.374a9.86 9.86 0 01-1.51-5.26c.001-5.45 4.436-9.884 9.888-9.884 2.64 0 5.122 1.03 6.988 2.898a9.825 9.825 0 012.893 6.994c-.003 5.45-4.437 9.884-9.885 9.884m8.413-18.297A11.815 11.815 0 0012.05 0C5.495 0 .16 5.335.157 11.892c0 2.096.547 4.142 1.588 5.945L.057 24l6.305-1.654a11.882 11.882 0 005.683 1.448h.005c6.554 0 11.89-5.335 11.893-11.893a11.821 11.821 0 00-3.48-8.413z"/></svg>
                    </div>
                    <div>
                        <div class="share-btn-label">שתף ב-WhatsApp</div>
                        <div class="share-btn-sub">שלח ישירות לחבר או לקבוצה</div>
                    </div>
                </button>
                <button class="share-btn" id="shareNativeBtn">
                    <div class="share-btn-icon" style="background:#eff6ff;">
                        <svg viewBox="0 0 24 24" fill="none" stroke="#2563eb" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z"/></svg>
                    </div>
                    <div>
                        <div class="share-btn-label">שתף</div>
                        <div class="share-btn-sub">אפשרויות שיתוף נוספות</div>
                    </div>
                </button>
            </div>
            <div class="share-divider"><div class="share-divider-line"></div><div class="share-divider-text">או העתק קישור</div><div class="share-divider-line"></div></div>
            <div class="share-link-row">
                <input class="share-link-input" id="shareLinkInput" readonly>
                <button class="share-copy-btn" id="shareCopyBtn" onclick="copyShareLink()">העתק</button>
            </div>
        </div>
    </div>

    <!-- ══ EMPLOYER MODAL ══ -->
    <div class="employer-modal-backdrop" id="employerModal" onclick="if(event.target===this)closeEmployerModal()">
        <div class="employer-modal">
            <div class="employer-modal-hero" style="position:relative;overflow:hidden;">
                <div style="position:relative;z-index:1;">
                    <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:10px;">
                        <div style="display:inline-flex;align-items:center;gap:6px;background:rgba(255,255,255,0.18);border:1px solid rgba(255,255,255,0.3);color:white;font-size:11px;font-weight:800;padding:4px 12px;border-radius:99px;">
                            <svg width="10" height="10" fill="white" viewBox="0 0 24 24"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>
                            לוח מעסיקים
                        </div>
                        <button onclick="closeEmployerModal()" style="width:30px;height:30px;background:rgba(255,255,255,0.2);border:none;border-radius:50%;cursor:pointer;display:flex;align-items:center;justify-content:center;color:white;">
                            <svg width="13" height="13" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12"/></svg>
                        </button>
                    </div>
                    <div style="font-size:22px;font-weight:900;color:white;line-height:1.2;margin-bottom:4px;">פרסמו משרות<br>והגיעו לאלפי מועמדים</div>
                    <div style="font-size:12.5px;color:rgba(255,255,255,0.75);font-weight:600;">מלאו את הפרטים ונחזור אליכם תוך 24 שעות</div>
                </div>
            </div>
            <div class="employer-modal-scroll" id="employerModalScroll">
                <div class="employer-field">
                    <label>שם החברה <span>*</span></label>
                    <input class="employer-input" id="emp_company" placeholder="לדוגמה: חברת ABC בע״מ" type="text">
                </div>
                <div class="employer-field">
                    <label>שם איש הקשר <span>*</span></label>
                    <input class="employer-input" id="emp_contact" placeholder="שם פרטי ומשפחה" type="text">
                </div>
                <div class="employer-field">
                    <label>טלפון <span>*</span></label>
                    <input class="employer-input" id="emp_phone" placeholder="05X-XXXXXXX" type="tel" dir="ltr">
                </div>
                <div class="employer-field">
                    <label>אימייל</label>
                    <input class="employer-input" id="emp_email" placeholder="company@example.com" type="email" dir="ltr">
                </div>
                <div class="employer-field">
                    <label>גודל החברה</label>
                    <select class="employer-input employer-select" id="emp_size">
                        <option value="">בחרו גודל</option>
                        <option>1-10 עובדים</option>
                        <option>11-50 עובדים</option>
                        <option>51-200 עובדים</option>
                        <option>201-500 עובדים</option>
                        <option>500+ עובדים</option>
                    </select>
                </div>
                <div class="employer-field">
                    <label>כמות משרות לפרסום</label>
                    <select class="employer-input employer-select" id="emp_count">
                        <option value="">בחרו כמות</option>
                        <option>1 משרה</option>
                        <option>2-5 משרות</option>
                        <option>6-10 משרות</option>
                        <option>10+ משרות</option>
                    </select>
                </div>
                <div class="employer-field">
                    <label>תקציב חודשי לפרסום</label>
                    <div class="employer-chips-row" id="empBudgetChips">
                        <div class="employer-chip" onclick="selectEmployerChip(this,'budget')">עד ₪500</div>
                        <div class="employer-chip" onclick="selectEmployerChip(this,'budget')">₪500-₪1,500</div>
                        <div class="employer-chip" onclick="selectEmployerChip(this,'budget')">₪1,500-₪3,000</div>
                        <div class="employer-chip" onclick="selectEmployerChip(this,'budget')">₪3,000+</div>
                    </div>
                </div>
                <div class="employer-field">
                    <label>סוג משרה</label>
                    <div class="employer-chips-row" id="empTypeChips">
                        <div class="employer-chip" onclick="selectEmployerChip(this,'type')">משמרות / ארעי</div>
                        <div class="employer-chip" onclick="selectEmployerChip(this,'type')">משרה קבועה</div>
                        <div class="employer-chip" onclick="selectEmployerChip(this,'type')">שניהם</div>
                    </div>
                </div>
                <div class="employer-field">
                    <label>הערות נוספות</label>
                    <textarea class="employer-input" id="emp_notes" rows="3" placeholder="תיאור קצר, דרישות מיוחדות, או כל מה שחשוב לדעת..." style="resize:none;"></textarea>
                </div>
                <button class="employer-submit-btn" onclick="submitEmployerForm()">
                    <svg width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"/></svg>
                    שלחו פרטים — נחזור בהקדם
                </button>
                <div style="text-align:center;font-size:11.5px;color:#94a3b8;font-weight:600;margin-top:10px;padding-bottom:4px;">פנייה ללא עלות ✓ מענה תוך 24 שעות</div>
            </div>
        </div>
    </div>

    <script>
    // ══ SHARE POPUP LOGIC ══
    let _shareJob = {};
    function openSharePopup(title, company, id) {
        _shareJob = { title, company, id };
        const url = window.location.origin + window.location.pathname + '?job=' + encodeURIComponent(id);
        document.getElementById('shareJobTitle').textContent = title;
        document.getElementById('shareJobSub').textContent = 'Cheetah Jobs';
        document.getElementById('shareLinkInput').value = url;
        document.getElementById('shareCopyBtn').textContent = 'העתק';
        document.getElementById('shareCopyBtn').classList.remove('copied');

        const msg = `🐆 *Cheetah Jobs*\n*${title}*\n\nלפרטים ולהרשמה:\n${url}`;

        document.getElementById('shareWhatsAppBtn').onclick = () => {
            window.open('https://wa.me/?text=' + encodeURIComponent(msg), '_blank');
        };
        document.getElementById('shareNativeBtn').onclick = () => {
            if (navigator.share) {
                navigator.share({ title: title, text: company || '', url: url }).catch(() => {});
            } else {
                // fallback: copy
                copyShareLink();
            }
        };
        document.getElementById('sharePopup').classList.add('active');
        document.body.style.overflow = 'hidden';
    }
    function closeSharePopup() {
        document.getElementById('sharePopup').classList.remove('active');
        document.body.style.overflow = '';
    }
    function copyShareLink() {
        const inp = document.getElementById('shareLinkInput');
        inp.select();
        try { document.execCommand('copy'); } catch(e) { navigator.clipboard && navigator.clipboard.writeText(inp.value); }
        const btn = document.getElementById('shareCopyBtn');
        btn.textContent = '✓ הועתק';
        btn.classList.add('copied');
        setTimeout(() => { btn.textContent = 'העתק'; btn.classList.remove('copied'); }, 2500);
    }

    // ══ EMPLOYER MODAL LOGIC ══
    let _empBudget = '', _empType = '';
    function selectEmployerChip(el, group) {
        const container = el.parentElement;
        container.querySelectorAll('.employer-chip').forEach(c => c.classList.remove('selected'));
        el.classList.add('selected');
        if (group === 'budget') _empBudget = el.textContent;
        if (group === 'type')   _empType   = el.textContent;
    }
    function openEmployerModal() {
        document.getElementById('employerModal').classList.add('active');
        document.getElementById('employerModalScroll').scrollTop = 0;
        document.body.style.overflow = 'hidden';
    }
    function closeEmployerModal() {
        document.getElementById('employerModal').classList.remove('active');
        document.body.style.overflow = '';
    }
    function submitEmployerForm() {
        const company  = document.getElementById('emp_company').value.trim();
        const contact  = document.getElementById('emp_contact').value.trim();
        const phone    = document.getElementById('emp_phone').value.trim();
        if (!company) { Swal.fire({icon:'warning',title:'שם החברה חסר',confirmButtonColor:'#6366f1'}); return; }
        if (!contact) { Swal.fire({icon:'warning',title:'שם איש הקשר חסר',confirmButtonColor:'#6366f1'}); return; }
        if (!isValidPhone(phone)) { Swal.fire({icon:'error',title:'מספר טלפון לא תקין',text:'נא להזין מספר נייד תקין',confirmButtonColor:'#6366f1'}); return; }
        const email = document.getElementById('emp_email').value.trim();
        const size  = document.getElementById('emp_size').value;
        const count = document.getElementById('emp_count').value;
        const notes = document.getElementById('emp_notes').value.trim();
        const fd = new FormData();
        fd.append('company', company);
        fd.append('contact', contact);
        fd.append('phone',   phone);
        fd.append('email',   email);
        fd.append('size',    size);
        fd.append('count',   count);
        fd.append('budget',  _empBudget);
        fd.append('jobType', _empType);
        fd.append('notes',   notes);
        const btn = document.querySelector('.employer-submit-btn');
        const orig = btn.innerHTML;
        btn.disabled = true;
        btn.innerHTML = '<svg class="animate-spin" style="width:20px;height:20px;" fill="none" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" class="opacity-25"/><path fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" class="opacity-75"/></svg>';
        fetch(CONFIG.EMPLOYER_API_URL, { method: 'POST', body: fd })
            .then(r => r.json())
            .then(data => {
                closeEmployerModal();
                Swal.fire({ title: 'הפרטים נשלחו!', text: 'נחזור אליכם תוך 24 שעות.', icon: 'success', confirmButtonText: 'תודה', confirmButtonColor: '#6366f1' });
            })
            .catch(() => {
                Swal.fire({ title: 'אופס...', text: 'הייתה בעיה. ניתן לפנות ל-jobscohen1@gmail.com', icon: 'error', confirmButtonColor: '#ef4444' });
            })
            .finally(() => { btn.disabled = false; btn.innerHTML = orig; });
    }

    // ══ FUZZY SEARCH (Hebrew typo correction) ══
    const FUZZY_DICT = [
        'אבטחה','מחסן','מחסנאי','נהג','נהיגה','מלצר','מלצרות','קופאי','קופאית',
        'שמירה','שומר','ניקיון','מנקה','בנייה','בניין','פועל','פועלת','חשמלאי',
        'אינסטלטור','נגר','צבעי','גנן','גינון','מטבח','שף','בישול','אפייה',
        'לוגיסטיקה','שילוח','משרד','פקיד','פקידה','מזכיר','מזכירה','חשב','הנהלת חשבונות',
        'שיווק','מכירות','מוכר','מוכרת','קמעונאות','אחסנה','עמסה','פריקה','הובלה',
        'סיעוד','מטפל','מטפלת','סייעת','עובד סוציאלי','ילדים','גן ילדים','אחות',
        'מחשבים','תוכנה','מתכנת','מתכנתת','אדמין','תמיכה טכנית','IT'
    ];
    function levenshtein(a, b) {
        const m = a.length, n = b.length;
        const dp = Array.from({length: m+1}, (_, i) => Array.from({length: n+1}, (_, j) => j === 0 ? i : 0));
        for (let j = 1; j <= n; j++) dp[0][j] = j;
        for (let i = 1; i <= m; i++) for (let j = 1; j <= n; j++) {
            dp[i][j] = a[i-1] === b[j-1] ? dp[i-1][j-1] : 1 + Math.min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]);
        }
        return dp[m][n];
    }
    function findFuzzySuggestion(query) {
        if (!query || query.length < 3) return null;
        const q = query.trim();
        // Check if query already matches something in the data
        const allTerms = [...allTempShiftsData, ...allPermJobsData].map(x => x ? (x.desc || x.title || '') : '').filter(Boolean);
        const exactHit = allTerms.some(t => t.includes(q));
        if (exactHit) return null;
        let best = null, bestDist = Infinity;
        for (const word of FUZZY_DICT) {
            const dist = levenshtein(q, word);
            const threshold = Math.max(1, Math.floor(word.length * 0.3));
            if (dist <= threshold && dist < bestDist) { best = word; bestDist = dist; }
        }
        return best && best !== q ? best : null;
    }
    let _fuzzyDebounce;
    const _origApplyFilters = applyFilters;
    // Patch applyFilters to show fuzzy suggestion
    const _patchedApplyFilters = function() {
        _origApplyFilters();
        clearTimeout(_fuzzyDebounce);
        _fuzzyDebounce = setTimeout(() => {
            const q = document.getElementById('searchInput').value.trim();
            const sug = findFuzzySuggestion(q);
            const el = document.getElementById('fuzzySuggestion');
            if (sug) {
                document.getElementById('fuzzySuggestionText').innerHTML = `התכוונת ל: <strong>${sug}</strong>?`;
                document.getElementById('fuzzySuggestionAccept').onclick = () => {
                    document.getElementById('searchInput').value = sug;
                    el.classList.remove('visible');
                    applyFilters();
                };
                el.classList.add('visible');
            } else {
                el.classList.remove('visible');
            }
        }, 400);
    };
    // Override applyFilters to include fuzzy
    window.applyFilters = function() {
        _patchedApplyFilters();
    };

    // close share/employer on Escape
    document.addEventListener('keydown', e => {
        if (e.key === 'Escape') { closeSharePopup(); closeEmployerModal(); }
    });
    </script>
</body>
</html>
