import { useState, useEffect, useRef } from 'react';
import { ElevatorDoor } from './components/ElevatorDoor';
import { FloorDisplay } from './components/FloorDisplay';
import { ControlButton } from './components/ControlButton';
import { ChevronUp, ChevronDown } from 'lucide-react';

const MIN_FLOOR = 1;
const MAX_FLOOR = 15;
const MOVE_DELAY = 1000; // 1 second per floor

export default function App() {
  const [calledFromFloor] = useState<number>(() =>
    Math.floor(Math.random() * MAX_FLOOR) + MIN_FLOOR
  );
  const [currentFloor, setCurrentFloor] = useState<number>(1);
  const [isMoving, setIsMoving] = useState(false);
  const [direction, setDirection] = useState<'up' | 'down' | null>(null);
  const [doorsOpen, setDoorsOpen] = useState(false);
  const [wsConnected, setWsConnected] = useState(false);
  const wsRef = useRef<WebSocket | null>(null);

  // 내가 요청한 버튼 상태 (불 켜기/끄기)
  const [myUpButtonActive, setMyUpButtonActive] = useState(false);
  const [myDownButtonActive, setMyDownButtonActive] = useState(false);

  // WebSocket 연결 설정
  useEffect(() => {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = `${protocol}//${window.location.host}/ws/elevator`;

    console.log('Connecting to WebSocket:', wsUrl);
    const ws = new WebSocket(wsUrl);

    ws.onopen = () => {
      console.log('WebSocket connected');
      setWsConnected(true);
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      console.log('Received from server:', data);

      if (data.type === 'ELEVATOR_STATUS') {
        if (data.currentFloor !== undefined) {
          setCurrentFloor(data.currentFloor);
        }
        if (data.direction !== undefined) {
          setDirection(data.direction ? data.direction.toLowerCase() : null);
        }
        if (data.isMoving !== undefined) {
          setIsMoving(data.isMoving);
        }
        if (data.doorsOpen !== undefined) {
          setDoorsOpen(data.doorsOpen);

          // 엘리베이터가 내 층에 도착하면 버튼 불 끄기
          if (data.doorsOpen && data.currentFloor === calledFromFloor) {
            setMyUpButtonActive(false);
            setMyDownButtonActive(false);
          }
        }
      }
    };

    ws.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    ws.onclose = () => {
      console.log('WebSocket disconnected');
      setWsConnected(false);
    };

    wsRef.current = ws;

    return () => {
      ws.close();
    };
  }, []);

  const handleMoveUp = () => {
    // WebSocket으로 서버에 요청 전송 - 내가 있는 층으로 호출
    if (wsRef.current && wsRef.current.readyState === WebSocket.OPEN) {
      const message = {
        type: 'CALL_ELEVATOR',
        targetFloor: calledFromFloor,
        direction: 'UP'
      };
      wsRef.current.send(JSON.stringify(message));
      console.log('Sent to server:', message);

      // 내 UP 버튼 불 켜기
      setMyUpButtonActive(true);
    }
  };

  const handleMoveDown = () => {
    // WebSocket으로 서버에 요청 전송 - 내가 있는 층으로 호출
    if (wsRef.current && wsRef.current.readyState === WebSocket.OPEN) {
      const message = {
        type: 'CALL_ELEVATOR',
        targetFloor: calledFromFloor,
        direction: 'DOWN'
      };
      wsRef.current.send(JSON.stringify(message));
      console.log('Sent to server:', message);

      // 내 DOWN 버튼 불 켜기
      setMyDownButtonActive(true);
    }
  };

  // Buttons are fixed based on the floor where elevator was called from
  const showUpButton = calledFromFloor < MAX_FLOOR;
  const showDownButton = calledFromFloor > MIN_FLOOR;

  return (
    <div className="min-h-screen flex items-center justify-center overflow-hidden relative bg-[#e8e8e8]">
      {/* WebSocket 연결 상태 표시 */}
      <div className="absolute top-4 right-4 z-20 flex items-center gap-2 bg-white/80 px-4 py-2 rounded-lg shadow-md">
        <div className={`w-2 h-2 rounded-full ${wsConnected ? 'bg-green-500' : 'bg-red-500'}`}></div>
        <span className="text-xs font-medium">{wsConnected ? 'Connected' : 'Disconnected'}</span>
      </div>

      {/* Subtle wall texture */}
      <div className="absolute inset-0 opacity-30" style={{
        backgroundImage: `linear-gradient(0deg, transparent 24%, rgba(0, 0, 0, .02) 25%, rgba(0, 0, 0, .02) 26%, transparent 27%, transparent 74%, rgba(0, 0, 0, .02) 75%, rgba(0, 0, 0, .02) 76%, transparent 77%, transparent),
        linear-gradient(90deg, transparent 24%, rgba(0, 0, 0, .02) 25%, rgba(0, 0, 0, .02) 26%, transparent 27%, transparent 74%, rgba(0, 0, 0, .02) 75%, rgba(0, 0, 0, .02) 76%, transparent 77%, transparent)`,
        backgroundSize: '50px 50px'
      }} />
      
      {/* Main elevator container */}
      <div className="relative z-10 flex items-center gap-8 px-8">
        {/* Elevator section with floor display and door */}
        <div className="flex flex-col items-center gap-6">
          {/* Floor Display Panel */}
          <FloorDisplay 
            floor={currentFloor} 
            direction={direction}
            isMoving={isMoving}
          />

          {/* Elevator Door - 내 층에 도착했을 때만 열림 */}
          <ElevatorDoor isOpen={doorsOpen && currentFloor === calledFromFloor} />
        </div>

        {/* Button Panel (beside the door) */}
        <div className="flex flex-col gap-4">
          {/* Small floor indicator above button panel - FIXED FLOOR */}
          <div className="bg-gradient-to-br from-[#d0d0d0] to-[#b8b8b8] p-3 rounded-lg shadow-lg border border-[#a0a0a0]">
            <div className="bg-gradient-to-br from-[#2a2a2a] to-[#1a1a1a] rounded px-4 py-3 shadow-inner border border-[#404040]">
              <div className="flex flex-col items-center gap-1">
                <div className="text-[0.6rem] text-[#888] tracking-wider" style={{ fontFamily: 'system-ui, sans-serif' }}>
                  FLOOR
                </div>
                <span className="fixed-floor-number">{calledFromFloor}</span>
              </div>
            </div>
          </div>

          {/* Button panel */}
          <div className="flex flex-col gap-6 bg-gradient-to-br from-[#d0d0d0] to-[#b8b8b8] p-6 rounded-lg shadow-lg border border-[#a0a0a0]">
            <div className="text-center mb-2" style={{ fontFamily: 'system-ui, sans-serif', fontSize: '0.75rem', color: '#555', letterSpacing: '0.05em' }}>
              CALL
            </div>
            {showUpButton && (
              <ControlButton
                onClick={handleMoveUp}
                disabled={false}
                icon={<ChevronUp className="w-6 h-6" />}
                active={myUpButtonActive}
                label="UP"
              />
            )}
            {showDownButton && (
              <ControlButton
                onClick={handleMoveDown}
                disabled={false}
                icon={<ChevronDown className="w-6 h-6" />}
                active={myDownButtonActive}
                label="DOWN"
              />
            )}
          </div>
        </div>
      </div>

      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Orbitron:wght@700&display=swap');
        
        * {
          font-family: 'Inter', system-ui, sans-serif;
        }

        .fixed-floor-number {
          font-family: 'Orbitron', monospace;
          font-size: 1.5rem;
          line-height: 1;
          color: #ff6b35;
          font-weight: 700;
        }
      `}</style>
    </div>
  );
}