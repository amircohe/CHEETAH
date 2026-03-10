tailwind.config = {
            theme: {
                extend: {
                    fontFamily: { sans: ['Heebo', 'sans-serif'] },
                    colors: { brand: { 500: '#3b82f6', 600: '#2563eb', 900: '#1e3a8a' } },
                    boxShadow: { 'soft': '0 20px 40px -15px rgba(0,0,0,0.05)', 'glow': '0 0 20px rgba(59, 130, 246, 0.5)' }
                }
            }
        }

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
